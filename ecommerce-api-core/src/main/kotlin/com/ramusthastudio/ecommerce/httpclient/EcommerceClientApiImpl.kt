package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.toList
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
        return merge(
            flowOf(BukalapakClientEngine(httpClient, commonSearchRequest).searchByRestful()),
            flowOf(BlibliClientEngine(httpClient, commonSearchRequest).searchByRestful()),
            flowOf(TokopediaClientEngine(httpClient, commonSearchRequest).searchByRestful())
        ).toList()
    }

    override suspend fun searchProduct(
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest
    ): CommonSearchResponse {
        return when (ecommerceSource) {
            EcommerceSource.BUKALAPAK_AUTH -> CommonSearchResponse()
            EcommerceSource.BUKALAPAK -> BukalapakClientEngine(httpClient, commonSearchRequest).searchByRestful()
            EcommerceSource.BLIBLI -> BlibliClientEngine(httpClient, commonSearchRequest).searchByRestful()
            EcommerceSource.TOKOPEDIA -> TokopediaClientEngine(httpClient, commonSearchRequest).searchByRestful()
            EcommerceSource.SHOPEE -> CommonSearchResponse()
        }
    }
}
