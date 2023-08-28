package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.mapper.convertBukalapakSearchResponse
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
import io.ktor.http.contentType
import io.ktor.http.path

class EcommerceClientApiImpl: EcommerceClientApi {
    override suspend fun searchProduct(
        ecommerceHost: EcommerceHost,
        commonSearchRequest: CommonSearchRequest
    ): CommonSearchResponse {
        HttpClientApi.create().use {
            if (ecommerceHost == EcommerceHost.BUKALAPAK) {
                val authRequest = EcommerceHost.BUKALAPAK_AUTH
                val xparam = commonSearchRequest.xparam

                val authResponse: HttpResponse = it.post {
                    url {
                        protocol = authRequest.protocol
                        host = authRequest.host
                        path(authRequest.path)
                        contentType(ContentType.Application.Json)
                        setBody(BukalapakAuthRequest())
                    }
                }

                val bukalapakAuthResponse = authResponse.body<BukalapakAuthResponse>()
                xparam["access_token"] = bukalapakAuthResponse.accessToken
            }

            val searchResponse: HttpResponse = it.get {
                constructHttpRequest(ecommerceHost, commonSearchRequest)
            }
            return convertBukalapakSearchResponse(searchResponse.body<BukalapakSearchResponse>())
        }
    }

    private fun HttpRequestBuilder.constructHttpRequest(
        ecommerceHost: EcommerceHost,
        request: CommonSearchRequest
    ) {
        when (ecommerceHost) {
            EcommerceHost.BLIBLI -> {
                return url {
                    protocol = ecommerceHost.protocol
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
                return url {
                    protocol = ecommerceHost.protocol
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
