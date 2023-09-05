package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.mapper.convertBukalapakSearchResponse
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

class BukalapakClientEngine(
    private val httpClient: HttpClient,
    private val browser: Browser,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val ecommerceSource = EcommerceSource.BUKALAPAK

    override suspend fun searchByRestful(): CommonSearchResponse = coroutineScope {
        val xparam = commonSearchRequest.xparam

        if (!httpClient.engine.toString().contains("MockEngine")) {
            val authRequest = EcommerceSource.BUKALAPAK_AUTH

            httpClient.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = authRequest.host

                    "common-headers".asResourceMap().map { header(it.key, it.value) }

                    path(authRequest.path)
                    contentType(ContentType.Application.Json)
                    setBody(BukalapakAuthRequest())
                }
            }.let { res -> xparam["access_token"] = res.body<BukalapakAuthResponse>().accessToken }
        }

        xparam["limit"] = "50"
        val searchResponse: HttpResponse = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ecommerceSource.host

                "common-headers".asResourceMap().map { header(it.key, it.value) }

                path(ecommerceSource.path)
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

        convertBukalapakSearchResponse(
            searchResponse.responseTime.timestamp,
            searchResponse.body<BukalapakSearchResponse>()
        )
    }

    override suspend fun searchByScraper(content: String?): CommonSearchResponse = coroutineScope {
        CommonSearchResponse()
    }
}

suspend fun bukalapakSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val searchRequest = parameter.commonSearchRequest
    return when (parameter.ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            BukalapakClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            BukalapakClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}
