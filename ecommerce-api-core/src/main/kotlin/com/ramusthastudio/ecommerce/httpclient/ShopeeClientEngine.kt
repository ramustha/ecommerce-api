package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.ramusthastudio.ecommerce.common.currencyFormat
import com.ramusthastudio.ecommerce.common.getJsonObject
import com.ramusthastudio.ecommerce.common.getJsonValue
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.SearchParameter
import io.ktor.client.HttpClient
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.http.set
import it.skrape.core.htmlDocument
import it.skrape.selects.html
import it.skrape.selects.html5.script
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import org.slf4j.LoggerFactory
import java.util.*

private class ShopeeClientEngine(
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

        performScraper(content, searchData)

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

    private fun performScraper(
        content: String?,
        searchData: MutableList<CommonSearchResponse.Data>
    ) {
        Optional.ofNullable(content)
            .ifPresentOrElse({
                log.debug("extracted from file")
                extractContent(it, searchData)
            }, {
                log.debug("extracted from url")

                val urlBuilder = URLBuilder()
                urlBuilder.set {
                    protocol = URLProtocol.HTTPS
                    host = ecommerceSource.scraperHost

                    path(ecommerceSource.scraperPath)
                    parameters.append("keyword", commonSearchRequest.query)
                    parameters.append("page", commonSearchRequest.page.toInt().minus(1).toString())
                }
                browser.newPage().use { page ->
                    page.navigate(urlBuilder.build().toString())
                    page.waitForTimeout(EcommerceClientApiImpl.SCRAPER_PAGE_TIMEOUT_MILLIS)
                    page.keyboard().down("End")
                    extractContent(page.content(), searchData)
                }
            })
    }

    private fun extractContent(
        availableContent: String,
        searchData: MutableList<CommonSearchResponse.Data>
    ) {
        htmlDocument(availableContent) {
            script {
                withAttribute = "type" to "application/ld+json"
                val json = findAll { replaceScriptTag(this@findAll.html) }
                log.debug("available content = {}", json)

                Json.parseToJsonElement(json).jsonArray.filterNotNull()
                    .filter { it.getJsonValue("@type").equals("Product", ignoreCase = true) }
                    .forEach { productJson -> searchData.add(extractProductData(productJson)) }
            }
        }
    }

    private fun extractProductData(productJson: JsonElement): CommonSearchResponse.Data {
        val productId = productJson.getJsonValue("productID")
        val name = productJson.getJsonValue("name")
        val url = productJson.getJsonValue("url")
        val image = productJson.getJsonValue("image")

        val offers = productJson.getJsonObject("offers")
        val price = offers?.getJsonValue("price")?.currencyFormat()
        val lowPrice = offers?.getJsonValue("lowPrice")?.currencyFormat()
        val highPrice = offers?.getJsonValue("highPrice")?.currencyFormat()

        return CommonSearchResponse.Data(
            id = productId,
            name = name,
            price = price,
            lowPrice = lowPrice,
            highPrice = highPrice,
            url = url,
            imagesUrl = image,
        )
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
    LoggerFactory.getLogger("ShopeeClientEngine").debug("actual engine = {}", ecommerceEngine)
    return when (ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            ShopeeClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            ShopeeClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}

