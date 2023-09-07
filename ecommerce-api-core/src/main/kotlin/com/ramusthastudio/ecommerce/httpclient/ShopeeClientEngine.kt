package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.LoadState
import com.ramusthastudio.ecommerce.common.getJsonObject
import com.ramusthastudio.ecommerce.common.getJsonValue
import com.ramusthastudio.ecommerce.common.toBigDecimalOr
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.SearchParameter
import io.ktor.client.HttpClient
import it.skrape.core.htmlDocument
import it.skrape.selects.html
import it.skrape.selects.html5.script
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.*

class ShopeeClientEngine(
    private val httpClient: HttpClient,
    private val browser: Browser,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val ecommerceSource = EcommerceSource.SHOPEE

    override suspend fun searchByRestful(): CommonSearchResponse = coroutineScope {
        TODO("Please implement search by restful first")
    }

    override suspend fun searchByScraper(content: String?): CommonSearchResponse = coroutineScope {
        val starTime = System.currentTimeMillis()
        val searchData = mutableListOf<CommonSearchResponse.Data>()

        Optional.ofNullable(content)
            .ifPresentOrElse({
                log.debug("extracted from file")
                extractContent(it, searchData)
            }, {
                log.debug("extracted from url")

                val url = ecommerceSource.scraperHost + ecommerceSource.scraperPath + "/" + commonSearchRequest.query

                val page: Page = browser.newPage()
                page.navigate(
                    "https://shopee.co.id/search?keyword=${commonSearchRequest.query}&page=0"
                )
                page.waitForLoadState(LoadState.NETWORKIDLE)
                page.keyboard().down("End")
                extractContent(page.content(), searchData)
            })

        val processTime = System.currentTimeMillis() - starTime
        log.debug("process time (SCRAPE)= $processTime")

        CommonSearchResponse(
            searchData,
            CommonSearchResponse.Meta(
                source = ecommerceSource.toString(),
                processTime = processTime
            )
        )
    }

    private fun extractContent(
        availableContent: String,
        searchData: MutableList<CommonSearchResponse.Data>
    ) {
        htmlDocument(availableContent) {
            log.debug("available content = {}", availableContent)
            script {
                withAttribute = "type" to "application/ld+json"
                val json = findAll { replaceScriptTag(this@findAll.html) }
                val jsonElement = Json.parseToJsonElement(json).jsonArray.filterNotNull()
                jsonElement.filter { it.getJsonValue("@type").equals("Product", ignoreCase = true) }
                    .forEach {
                        val productId = it.getJsonValue("productID")
                        val name = it.getJsonValue("name")
                        val url = it.getJsonValue("url")
                        val image = it.getJsonValue("image")

                        val offers = it.getJsonObject("offers")
                        val price =
                            offers?.getJsonValue("Price")?.toBigDecimalOr(BigDecimal.valueOf(-1))
                        val lowPrice =
                            offers?.getJsonValue("lowPrice")?.toBigDecimalOr(BigDecimal.valueOf(-1))
                        val highPrice =
                            offers?.getJsonValue("highPrice")?.toBigDecimalOr(BigDecimal.valueOf(-1))

                        val productData = CommonSearchResponse.Data(
                            id = productId,
                            name = name,
                            price = price?.setScale(0),
                            lowPrice = lowPrice?.setScale(0),
                            highPrice = highPrice?.setScale(0),
                            url = url,
                            imagesUrl = image,
                        )
                        searchData.add(productData)
                    }
            }
        }
    }
}

suspend fun shopeeSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val ecommerceEngine = EcommerceEngine.SCRAPER
    val searchRequest = parameter.commonSearchRequest
    return when (ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            ShopeeClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            ShopeeClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}

