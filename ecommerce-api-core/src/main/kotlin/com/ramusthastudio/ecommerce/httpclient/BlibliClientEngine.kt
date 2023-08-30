package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.mapper.convertBlibliSearchResponse
import com.ramusthastudio.ecommerce.model.BlibliSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.path

class BlibliClientEngine(
    private val httpClient: HttpClient,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val ecommerceSource = EcommerceSource.BLIBLI

    override suspend fun searchByRestful(): CommonSearchResponse {
        val xparam = commonSearchRequest.xparam
        xparam.clear() // reset

        xparam["channelId"] = "web"
        val searchResponse: HttpResponse = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ecommerceSource.host

                "common-headers".asResourceMap().map { header(it.key, it.value) }

                path(ecommerceSource.path)
                parameters.append("page", commonSearchRequest.page)
                parameters.append("start", commonSearchRequest.offset)
                parameters.append("searchTerm", commonSearchRequest.query)
                xparam.forEach {
                    parameters.append(it.key, it.value)
                }
            }
        }
        return convertBlibliSearchResponse(
            searchResponse.responseTime.timestamp,
            searchResponse.body<BlibliSearchResponse>()
        )
    }

    override suspend fun searchByScraper(): CommonSearchResponse {
        return CommonSearchResponse()
    }
}
