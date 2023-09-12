package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
        BrowserType.LaunchOptions().setHeadless(true)
    )
    private var currentState: Boolean = true

    companion object {
        const val SCRAPER_PAGE_TIMEOUT_MILLIS: Double = 10000.0
    }

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
        playWright.close()
        log.info("Playwright Closed")
    }

    override suspend fun searchProductCombine(
        commonSearchRequest: CommonSearchRequest
    ): List<CommonSearchResponse> = coroutineScope {
        val engine = usedEngine(commonSearchRequest)
        val searchParameter = SearchParameter(commonSearchRequest, engine)
        val bukalapakSearch = async {
            bukalapakSearch(httpClient, browser) { searchParameter }
        }
        val tokopediaSearch = async {
            tokopediaSearch(httpClient, browser) { searchParameter }
        }
        val blibliSearch = async {
            blibliSearch(httpClient, browser) { searchParameter }
        }
        val shopeeSearch = async {
            shopeeSearch(httpClient, browser) { searchParameter }
        }
        listOf(
            bukalapakSearch.await(),
            tokopediaSearch.await(),
            blibliSearch.await(),
            shopeeSearch.await(),
        )
            .sortedBy { it.meta.processTime }
            .apply { currentState = !currentState }
    }

    override suspend fun searchProduct(
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest,
        content: String?
    ): CommonSearchResponse {
        val engine = usedEngine(commonSearchRequest)
        val searchParameter = SearchParameter(commonSearchRequest, engine, content)
        return when (ecommerceSource) {
            BUKALAPAK_AUTH -> CommonSearchResponse()
            BUKALAPAK -> bukalapakSearch(httpClient, browser) { searchParameter }
            BLIBLI -> blibliSearch(httpClient, browser) { searchParameter }
            TOKOPEDIA -> tokopediaSearch(httpClient, browser) { searchParameter }
            SHOPEE -> shopeeSearch(httpClient, browser) { searchParameter }
        }.apply { currentState = !currentState }
    }

    private fun usedEngine(commonSearchRequest: CommonSearchRequest): EcommerceEngine {
        val switchEngine = if (currentState) RESTFUL else SCRAPER
        val engine = commonSearchRequest.engine?.let { EcommerceEngine.valueOf(it) } ?: switchEngine
        log.info("engine = $engine")
        return engine
    }
}
