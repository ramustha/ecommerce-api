package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.WaitUntilState
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
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
        CommonSearchResponse()
    }

    override suspend fun searchByScraper(content: String?): CommonSearchResponse = coroutineScope {
        val starTime = System.currentTimeMillis()
        val searchData = mutableListOf<CommonSearchResponse.Data>()

        Optional.ofNullable(content)
            .ifPresentOrElse({
                extractContent(it, searchData)
            }, {
                val url = ecommerceSource.host + ecommerceSource.path + "/" + commonSearchRequest.query
                val navigateOptions = Page.NavigateOptions()
                navigateOptions.setWaitUntil(WaitUntilState.LOAD)

                val page: Page = browser.newPage()
                page.navigate(
                    "https://shopee.co.id/search?keyword=${commonSearchRequest.query}&page=0",
                    navigateOptions
                )
                page.keyboard().down("End")
                extractContent(page.content(), searchData)
            })

        CommonSearchResponse(
            searchData,
            CommonSearchResponse.Meta(
                source = ecommerceSource.toString(),
                priority = System.currentTimeMillis(),
                responseTime = System.currentTimeMillis() - starTime
            )
        )
    }

    private fun extractContent(
        availableContent: String,
        searchData: MutableList<CommonSearchResponse.Data>
    ) {
        htmlDocument(availableContent) {
            script {
                withAttribute = "type" to "application/ld+json"
                val json = findAll { replaceScriptTag(this@findAll.html) }
                val jsonElement = Json.parseToJsonElement(json).jsonArray.filterNotNull()
                jsonElement.filter { it.jsonObject["@type"]?.jsonPrimitive?.content == "Product" }
                    .forEach {
                        log.debug("scrape content = {}", it)

                        val offers = it.jsonObject["offers"]?.jsonObject
                        val priceStr = offers?.get("Price")?.jsonPrimitive?.content
                        val lowPriceStr = offers?.get("lowPrice")?.jsonPrimitive?.content
                        val highPriceStr = offers?.get("highPrice")?.jsonPrimitive?.content

                        val price = priceStr?.toBigDecimalOrNull() ?: BigDecimal("-1")
                        val lowPrice = lowPriceStr?.toBigDecimalOrNull() ?: BigDecimal("-1")
                        val highPrice = highPriceStr?.toBigDecimalOrNull() ?: BigDecimal("-1")

                        val productData = CommonSearchResponse.Data(
                            id = it.jsonObject["productID"].toString(),
                            name = it.jsonObject["name"].toString(),
                            price = price,
                            lowPrice = lowPrice,
                            highPrice = highPrice,
                            url = it.jsonObject["url"].toString(),
                            imagesUrl = it.jsonObject["image"].toString()
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
    val searchRequest = parameter.commonSearchRequest
    return when (parameter.ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            ShopeeClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            ShopeeClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}

