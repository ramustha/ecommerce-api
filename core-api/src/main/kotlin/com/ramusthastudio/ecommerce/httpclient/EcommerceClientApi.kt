package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.BukalapakAuthRequest
import com.ramusthastudio.ecommerce.model.BukalapakAuthResponse
import com.ramusthastudio.ecommerce.model.CommonWebRequest
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

class EcommerceClientApi {
    suspend fun searchProduct(ecommerceHost: EcommerceHost, commonWebRequest: CommonWebRequest) {
        HttpClientApi.create().use {
            if (ecommerceHost == EcommerceHost.BUKALAPAK) {
                val authRequest = EcommerceHost.BUKALAPAK_AUTH
                val xparam = commonWebRequest.xparam

                val response: HttpResponse = it.post {
                    url {
                        protocol = authRequest.protocol
                        host = authRequest.host
                        path(authRequest.path)
                        contentType(ContentType.Application.Json)
                        setBody(BukalapakAuthRequest())
                    }
                }

                val bukalapakAuthResponse: BukalapakAuthResponse = response.body()
                xparam["access_token"] = bukalapakAuthResponse.accessToken
            }

            val bukalapakResponse: HttpResponse = it.get {
                constructHttpRequest(ecommerceHost, commonWebRequest)
            }
            println(bukalapakResponse.status)
        }
    }

    private fun HttpRequestBuilder.constructHttpRequest(
        ecommerceHost: EcommerceHost,
        request: CommonWebRequest
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
