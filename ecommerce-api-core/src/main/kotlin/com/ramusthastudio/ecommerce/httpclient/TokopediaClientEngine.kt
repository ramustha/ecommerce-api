package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.mapper.convertTokopediaSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.SearchParameter
import com.ramusthastudio.ecommerce.model.TokopediaSearchResponse
import com.ramusthastudio.ecommerce.model.constructTokopediaBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.coroutineScope

class TokopediaClientEngine(
    private val httpClient: HttpClient,
    private val browser: Browser,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val ecommerceSource = EcommerceSource.TOKOPEDIA

    override suspend fun searchByRestful(): CommonSearchResponse = coroutineScope {
        val searchResponse: HttpResponse = httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = ecommerceSource.host

                "common-headers".asResourceMap().map { header(it.key, it.value) }

                path(ecommerceSource.path)
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Referrer, "https://www.tokopedia.com")
                setBody(listOf(constructTokopediaBody(commonSearchRequest.query)))
            }
        }

        convertTokopediaSearchResponse(
            searchResponse.responseTime.timestamp,
            searchResponse.body<List<TokopediaSearchResponse>>().first()
        )
    }

    override suspend fun searchByScraper(content: String?): CommonSearchResponse = coroutineScope {
        CommonSearchResponse()
    }
}

suspend fun tokopediaSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val searchRequest = parameter.commonSearchRequest
    return when (parameter.ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            TokopediaClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            TokopediaClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}
