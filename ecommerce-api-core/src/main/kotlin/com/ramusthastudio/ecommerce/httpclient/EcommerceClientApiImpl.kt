package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine.RESTFUL
import com.ramusthastudio.ecommerce.model.EcommerceEngine.SCRAPER
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.EcommerceSource.BLIBLI
import com.ramusthastudio.ecommerce.model.EcommerceSource.BUKALAPAK
import com.ramusthastudio.ecommerce.model.EcommerceSource.BUKALAPAK_AUTH
import com.ramusthastudio.ecommerce.model.EcommerceSource.SHOPEE
import com.ramusthastudio.ecommerce.model.EcommerceSource.TOKOPEDIA
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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EcommerceClientApiImpl(engine: HttpClientEngine = CIO.create()) : EcommerceClientApi {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val httpClient = initializeHttpClient(engine)
    private var currentState: Boolean = true

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
        log.info("HTTP Client Closed")
    }

    override suspend fun searchProductCombine(
        commonSearchRequest: CommonSearchRequest
    ): List<CommonSearchResponse> {
        val ecommerceEngine = if (currentState) RESTFUL else SCRAPER
        log.info("ecommerceEngine = $ecommerceEngine")

        return listOf(
            bukalapakSearch(httpClient, commonSearchRequest) { ecommerceEngine },
            tokopediaSearch(httpClient, commonSearchRequest) { ecommerceEngine },
            blibliSearch(httpClient, commonSearchRequest) { ecommerceEngine }
        )
            .sortedBy { it.meta.priority }
            .apply { currentState = !currentState }
    }

    override suspend fun searchProduct(
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest
    ): CommonSearchResponse {
        val ecommerceEngine = if (currentState) RESTFUL else SCRAPER
        log.info("ecommerceEngine = $ecommerceEngine")

        return when (ecommerceSource) {
            BUKALAPAK_AUTH -> CommonSearchResponse()
            BUKALAPAK -> bukalapakSearch(httpClient, commonSearchRequest) { ecommerceEngine }
            BLIBLI -> blibliSearch(httpClient, commonSearchRequest) { ecommerceEngine }
            TOKOPEDIA -> tokopediaSearch(httpClient, commonSearchRequest) { ecommerceEngine }
            SHOPEE -> CommonSearchResponse()
        }.apply { currentState = !currentState }
    }
}
