package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.common.commonSearchRequest
import com.ramusthastudio.ecommerce.mapper.convertBlibliSearchResponse
import com.ramusthastudio.ecommerce.mapper.convertBukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.BlibliSearchResponse
import com.ramusthastudio.ecommerce.model.BukalapakAuthRequest
import com.ramusthastudio.ecommerce.model.BukalapakAuthResponse
import com.ramusthastudio.ecommerce.model.BukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceHost
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class EcommerceClientApiImpl : EcommerceClientApi {
    override suspend fun searchProduct(
        ecommerceHost: EcommerceHost,
        query: String
    ): CommonSearchResponse {
        HttpClientApi.create().use {
            val commonSearchRequest: CommonSearchRequest = commonSearchRequest(query)

            if (ecommerceHost == EcommerceHost.BUKALAPAK) {
                val authRequest = EcommerceHost.BUKALAPAK_AUTH
                val xparam = commonSearchRequest.xparam

                it.post {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = authRequest.host
                        path(authRequest.path)
                        contentType(ContentType.Application.Json)
                        setBody(BukalapakAuthRequest())
                    }
                }.let { res -> xparam["access_token"] = res.body<BukalapakAuthResponse>().accessToken }
            }

            val searchResponse: HttpResponse = it.get {
                constructHttpRequest(ecommerceHost, commonSearchRequest)
            }

            when (ecommerceHost) {
                EcommerceHost.BUKALAPAK -> {
                    return convertBukalapakSearchResponse(searchResponse.body<BukalapakSearchResponse>())
                }

                EcommerceHost.BLIBLI -> {
                    return convertBlibliSearchResponse(searchResponse.body<BlibliSearchResponse>())
                }

                else -> {
                    throw IllegalArgumentException("Host not found!")
                }
            }
        }
    }

    private fun HttpRequestBuilder.constructHttpRequest(
        ecommerceHost: EcommerceHost,
        request: CommonSearchRequest
    ) {
        val xparam = request.xparam

        when (ecommerceHost) {
            EcommerceHost.BLIBLI -> {
                xparam["channelId"] = "web"
                return url {
                    protocol = URLProtocol.HTTPS
                    host = ecommerceHost.host
                    path(ecommerceHost.path)
                    parameters.append("page", request.page)
                    parameters.append("start", request.offset)
                    parameters.append("searchTerm", request.query)
                    request.xparam.forEach {
                        parameters.append(it.key, it.value)
                    }
                }
            }

            EcommerceHost.BUKALAPAK -> {
                xparam["limit"] = "50"
                return url {
                    protocol = URLProtocol.HTTPS
                    host = ecommerceHost.host
                    path(ecommerceHost.path)
                    parameters.append("page", request.page)
                    parameters.append("offset", request.offset)
                    parameters.append("keywords", request.query)
                    request.xparam.forEach {
                        parameters.append(it.key, it.value)
                    }
                }
            }

            else -> {
                throw IllegalArgumentException("Host not found!")
            }
        }
    }
}
