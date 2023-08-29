package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.common.commonSearchRequest
import com.ramusthastudio.ecommerce.mapper.convertBlibliSearchResponse
import com.ramusthastudio.ecommerce.mapper.convertBukalapakSearchResponse
import com.ramusthastudio.ecommerce.mapper.convertTokopediaSearchResponse
import com.ramusthastudio.ecommerce.model.BlibliSearchResponse
import com.ramusthastudio.ecommerce.model.BukalapakAuthRequest
import com.ramusthastudio.ecommerce.model.BukalapakAuthResponse
import com.ramusthastudio.ecommerce.model.BukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceHost
import com.ramusthastudio.ecommerce.model.TokopediaSearchResponse
import com.ramusthastudio.ecommerce.model.constructTokopediaBody
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.path
import io.ktor.http.userAgent
import kotlin.collections.set

class EcommerceClientApiImpl : EcommerceClientApi {
    private val userAgent =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36"

    override suspend fun searchProduct(
        ecommerceHost: EcommerceHost,
        query: String
    ): CommonSearchResponse {
        HttpClientApi.create().use {
            val commonSearchRequest: CommonSearchRequest = commonSearchRequest(query)

            when (ecommerceHost) {
                EcommerceHost.BUKALAPAK -> {
                    val authRequest = EcommerceHost.BUKALAPAK_AUTH
                    val xparam = commonSearchRequest.xparam

                    it.post {
                        url {
                            protocol = URLProtocol.HTTPS
                            host = authRequest.host
                            path(authRequest.path)
                            contentType(ContentType.Application.Json)
                            userAgent(userAgent)
                            setBody(BukalapakAuthRequest())
                        }
                    }.let { res -> xparam["access_token"] = res.body<BukalapakAuthResponse>().accessToken }

                    val searchResponse: HttpResponse = it.request {
                        constructHttpRequest(ecommerceHost, commonSearchRequest)
                    }
                    return convertBukalapakSearchResponse(searchResponse.body<BukalapakSearchResponse>())
                }

                EcommerceHost.BLIBLI -> {
                    val searchResponse: HttpResponse = it.request {
                        constructHttpRequest(ecommerceHost, commonSearchRequest)
                    }
                    return convertBlibliSearchResponse(searchResponse.body<BlibliSearchResponse>())
                }

                EcommerceHost.TOKOPEDIA -> {
                    val searchResponse: HttpResponse = it.request {
                        constructHttpRequest(ecommerceHost, commonSearchRequest)
                    }
                    val responseList = searchResponse.body<List<TokopediaSearchResponse>>()
                    return convertTokopediaSearchResponse(responseList.first())
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
                    headers {
                        append(HttpHeaders.UserAgent, userAgent)
                    }
                    protocol = URLProtocol.HTTPS
                    host = ecommerceHost.host
                    method = HttpMethod.Get
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
                    headers {
                        append(HttpHeaders.UserAgent, userAgent)
                    }
                    protocol = URLProtocol.HTTPS
                    host = ecommerceHost.host
                    method = HttpMethod.Get
                    path(ecommerceHost.path)
                    parameters.append("page", request.page)
                    parameters.append("offset", request.offset)
                    parameters.append("keywords", request.query)
                    request.xparam.forEach {
                        parameters.append(it.key, it.value)
                    }
                }
            }

            EcommerceHost.TOKOPEDIA -> {
                return url {
                    protocol = URLProtocol.HTTPS
                    host = ecommerceHost.host
                    method = HttpMethod.Post
                    path(ecommerceHost.path)
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.UserAgent, userAgent)
                    header(HttpHeaders.Referrer, "https://www.tokopedia.com")
                    setBody(listOf(constructTokopediaBody(request.query)))
                }
            }

            else -> {
                throw IllegalArgumentException("Host not found!")
            }
        }
    }
}
