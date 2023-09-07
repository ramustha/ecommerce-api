package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.common.convertBukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.BukalapakAuthRequest
import com.ramusthastudio.ecommerce.model.BukalapakAuthResponse
import com.ramusthastudio.ecommerce.model.BukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.SearchParameter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

class BukalapakClientEngine(
    private val httpClient: HttpClient,
    private val browser: Browser,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val ecommerceSource = EcommerceSource.BUKALAPAK

    override suspend fun searchByRestful(): CommonSearchResponse = coroutineScope {
        val xparam = commonSearchRequest.xparam

        if (!httpClient.engine.toString().contains("MockEngine")) {
            val authRequest = EcommerceSource.BUKALAPAK_AUTH

            httpClient.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = authRequest.restfulHost

                    "common-headers".asResourceMap().map { header(it.key, it.value) }

                    path(authRequest.restfulPath)
                    contentType(ContentType.Application.Json)
                    setBody(BukalapakAuthRequest())
                }
            }.let { res -> xparam["access_token"] = res.body<BukalapakAuthResponse>().accessToken }
        }

        xparam["limit"] = "50"
        val searchResponse: HttpResponse = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ecommerceSource.restfulHost

                "common-headers".asResourceMap().map { header(it.key, it.value) }

                path(ecommerceSource.restfulPath)
                parameters.append("page", commonSearchRequest.page)
                parameters.append("offset", commonSearchRequest.offset)
                parameters.append("keywords", commonSearchRequest.query)
                xparam.forEach {
                    parameters.append(it.key, it.value)
                }
            }
        }
        xparam.remove("access_token")
        xparam.remove("limit")
        searchResponse.call

        val processTime = searchResponse.responseTime.timestamp - searchResponse.requestTime.timestamp
        log.debug("process time (RESTFUL)= $processTime")

        convertBukalapakSearchResponse(
            processTime,
            searchResponse.body<BukalapakSearchResponse>()
        )
    }

    override suspend fun searchByScraper(content: String?): CommonSearchResponse = coroutineScope {
        TODO("Please implement search by scraper first")
    }
}

suspend fun bukalapakSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val ecommerceEngine = parameter.ecommerceEngine ?: EcommerceEngine.RESTFUL
    val searchRequest = parameter.commonSearchRequest
    return when (ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            BukalapakClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            BukalapakClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}
