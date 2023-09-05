package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
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
import com.ramusthastudio.ecommerce.model.SearchParameter
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

class EcommerceClientApiImpl(
    httpEngine: HttpClientEngine = CIO.create()
) : EcommerceClientApi {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val httpClient = initializeHttpClient(httpEngine)
    private val playWright: Playwright = Playwright.create()
    private val browser = playWright.firefox().launch(
        BrowserType.LaunchOptions().setHeadless(true).setSlowMo(1000.0)
    )
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
        playWright.close()
        log.info("HTTP Client Closed")
    }

    override suspend fun searchProductCombine(
        commonSearchRequest: CommonSearchRequest
    ): List<CommonSearchResponse> {
        val engine = if (currentState) RESTFUL else SCRAPER
        log.info("engine = $engine")

        val searchParameter = SearchParameter(commonSearchRequest, engine)
        return listOf(
            bukalapakSearch(httpClient, browser) { searchParameter },
            tokopediaSearch(httpClient, browser) { searchParameter },
            blibliSearch(httpClient, browser) { searchParameter },
            shopeeSearch(httpClient, browser) { SearchParameter(commonSearchRequest, SCRAPER) }
        )
            .sortedBy { it.meta.priority }
            .apply { currentState = !currentState }
    }

    override suspend fun searchProduct(
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest
    ): CommonSearchResponse {
        val engine = if (currentState) RESTFUL else SCRAPER
        log.info("engine = $engine")

        val searchParameter = SearchParameter(commonSearchRequest, engine)
        return when (ecommerceSource) {
            BUKALAPAK_AUTH -> CommonSearchResponse()
            BUKALAPAK -> bukalapakSearch(httpClient, browser) { searchParameter }
            BLIBLI -> blibliSearch(httpClient, browser) { searchParameter }
            TOKOPEDIA -> tokopediaSearch(httpClient, browser) { searchParameter }
            SHOPEE -> shopeeSearch(httpClient, browser) { SearchParameter(commonSearchRequest, SCRAPER) }
        }.apply { currentState = !currentState }
    }
}
