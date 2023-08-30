package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.mapper.convertTokopediaSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
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

class TokopediaClientEngine(
    private val httpClient: HttpClient,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val ecommerceSource = EcommerceSource.TOKOPEDIA

    override suspend fun searchByRestful(): CommonSearchResponse {
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
        val responseList = searchResponse.body<List<TokopediaSearchResponse>>()
        return convertTokopediaSearchResponse(
            searchResponse.responseTime.timestamp,
            responseList.first()
        )
    }

    override suspend fun searchByScraper(): CommonSearchResponse {
        return CommonSearchResponse()
    }
}

suspend fun tokopediaSearch(
    httpClient: HttpClient,
    commonSearchRequest: CommonSearchRequest,
    action: () -> EcommerceEngine
): CommonSearchResponse {
    return when (action()) {
        EcommerceEngine.RESTFUL -> TokopediaClientEngine(httpClient, commonSearchRequest).searchByRestful()
        EcommerceEngine.SCRAPER -> TokopediaClientEngine(httpClient, commonSearchRequest).searchByScraper()
    }
}
