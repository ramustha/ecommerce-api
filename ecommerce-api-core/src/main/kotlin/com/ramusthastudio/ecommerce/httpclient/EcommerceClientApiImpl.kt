package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

class EcommerceClientApiImpl(engine: HttpClientEngine = CIO.create()) : EcommerceClientApi {
    private val httpClient = initializeHttpClient(engine)

    @OptIn(ExperimentalSerializationApi::class)
    private fun initializeHttpClient(engine: HttpClientEngine) = HttpClient(engine) {
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

    override suspend fun searchProductCombine(
        commonSearchRequest: CommonSearchRequest
    ): List<CommonSearchResponse> {
        return listOf(
            bukalapakSearch(httpClient, commonSearchRequest) { EcommerceEngine.RESTFUL },
            tokopediaSearch(httpClient, commonSearchRequest) { EcommerceEngine.RESTFUL },
            blibliSearch(httpClient, commonSearchRequest) { EcommerceEngine.RESTFUL }
        ).sortedBy { it.meta.priority }
    }

    override suspend fun searchProduct(
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest
    ): CommonSearchResponse {
        return when (ecommerceSource) {
            EcommerceSource.BUKALAPAK_AUTH -> CommonSearchResponse()
            EcommerceSource.BUKALAPAK -> bukalapakSearch(httpClient, commonSearchRequest) { EcommerceEngine.RESTFUL }
            EcommerceSource.BLIBLI -> blibliSearch(httpClient, commonSearchRequest) { EcommerceEngine.RESTFUL }
            EcommerceSource.TOKOPEDIA -> tokopediaSearch(httpClient, commonSearchRequest) { EcommerceEngine.RESTFUL }
            EcommerceSource.SHOPEE -> CommonSearchResponse()
        }
    }
}
