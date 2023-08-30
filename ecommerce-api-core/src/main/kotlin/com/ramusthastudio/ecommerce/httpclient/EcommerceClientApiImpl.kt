package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.common.commonSearchRequest
import com.ramusthastudio.ecommerce.common.getResourceValue
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.collections.set

class EcommerceClientApiImpl(
    engine: HttpClientEngine = CIO.create()
) : EcommerceClientApi {
    private val httpClient = initializeHttpClient(engine)
    private val userAgent = "headers".getResourceValue("user-agent") ?: "curl/7.64.1"

    @OptIn(ExperimentalSerializationApi::class)
    private fun initializeHttpClient(
        engine: HttpClientEngine
    ) = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false

            })
        }
        install(HttpCache)
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    override fun close() {
        httpClient.close()
        println("HTTP Client Closed")
    }

    override suspend fun searchProduct(
        ecommerceHost: EcommerceHost,
        query: String
    ): CommonSearchResponse {
        val commonSearchRequest: CommonSearchRequest = commonSearchRequest(query)
        val xparam = commonSearchRequest.xparam
        return when (ecommerceHost) {
            EcommerceHost.BUKALAPAK_AUTH -> CommonSearchResponse()
            EcommerceHost.BUKALAPAK -> {
                if (!httpClient.engine.toString().contains("MockEngine")) {
                    val authRequest = EcommerceHost.BUKALAPAK_AUTH

                    httpClient.post {
                        url {
                            protocol = URLProtocol.HTTPS
                            host = authRequest.host
                            path(authRequest.path)
                            contentType(ContentType.Application.Json)
                            header(HttpHeaders.UserAgent, userAgent)
                            setBody(BukalapakAuthRequest())
                        }
                    }.let { res -> xparam["access_token"] = res.body<BukalapakAuthResponse>().accessToken }
                }

                xparam["limit"] = "50"
                val searchResponse: HttpResponse = httpClient.request {
                    url {
                        headers {
                            append(HttpHeaders.UserAgent, userAgent)
                        }
                        protocol = URLProtocol.HTTPS
                        host = ecommerceHost.host
                        method = HttpMethod.Get
                        path(ecommerceHost.path)
                        header(HttpHeaders.UserAgent, userAgent)
                        parameters.append("page", commonSearchRequest.page)
                        parameters.append("offset", commonSearchRequest.offset)
                        parameters.append("keywords", commonSearchRequest.query)
                        xparam.forEach {
                            parameters.append(it.key, it.value)
                        }
                    }
                }
                convertBukalapakSearchResponse(searchResponse.body<BukalapakSearchResponse>())
            }

            EcommerceHost.BLIBLI -> {
                xparam["channelId"] = "web"
                val searchResponse: HttpResponse = httpClient.request {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = ecommerceHost.host
                        method = HttpMethod.Get
                        path(ecommerceHost.path)
                        header(HttpHeaders.UserAgent, userAgent)
                        parameters.append("page", commonSearchRequest.page)
                        parameters.append("start", commonSearchRequest.offset)
                        parameters.append("searchTerm", commonSearchRequest.query)
                        xparam.forEach {
                            parameters.append(it.key, it.value)
                        }
                    }
                }
                convertBlibliSearchResponse(searchResponse.body<BlibliSearchResponse>())
            }

            EcommerceHost.TOKOPEDIA -> {
                val searchResponse: HttpResponse = httpClient.request {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = ecommerceHost.host
                        method = HttpMethod.Post
                        path(ecommerceHost.path)
                        contentType(ContentType.Application.Json)
                        header(HttpHeaders.UserAgent, userAgent)
                        header(HttpHeaders.Referrer, "https://www.tokopedia.com")
                        setBody(listOf(constructTokopediaBody(commonSearchRequest.query)))
                    }
                }
                val responseList = searchResponse.body<List<TokopediaSearchResponse>>()
                convertTokopediaSearchResponse(responseList.first())
            }

            EcommerceHost.SHOPEE -> CommonSearchResponse()
        }
    }
}
