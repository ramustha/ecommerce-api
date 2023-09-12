package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.common.convertBlibliSearchResponse
import com.ramusthastudio.ecommerce.common.currencyFormat
import com.ramusthastudio.ecommerce.common.getJsonObject
import com.ramusthastudio.ecommerce.common.getJsonValue
import com.ramusthastudio.ecommerce.model.BlibliSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.SearchParameter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.http.set
import it.skrape.core.htmlDocument
import it.skrape.selects.html
import it.skrape.selects.html5.script
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.slf4j.LoggerFactory
import java.util.*


private class BlibliClientEngine(
    private val httpClient: HttpClient,
    private val browser: Browser,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val ecommerceSource = EcommerceSource.BLIBLI

    override suspend fun searchByRestful(): CommonSearchResponse = coroutineScope {
        val xparam = commonSearchRequest.xparam
        xparam["channelId"] = "web"

        val searchResponse: HttpResponse = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ecommerceSource.restfulHost

                "common-headers".asResourceMap().map { header(it.key, it.value) }

                path(ecommerceSource.restfulPath)
                parameters.append("page", commonSearchRequest.page)
                parameters.append("start", commonSearchRequest.offset)
                parameters.append("searchTerm", commonSearchRequest.query)
                xparam.forEach {
                    parameters.append(it.key, it.value)
                }
            }
        }
        xparam.remove("channelId")

        val processTime = searchResponse.responseTime.timestamp - searchResponse.requestTime.timestamp
        log.debug("process time (RESTFUL)= $processTime")

        convertBlibliSearchResponse(
            processTime,
            searchResponse.body<BlibliSearchResponse>()
        )
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

                    path(ecommerceSource.scraperPath + "/" + commonSearchRequest.query)
                    parameters.append("page", commonSearchRequest.page)
                    parameters.append("start", commonSearchRequest.offset)
                }
                val page: Page = browser.newPage()
                page.navigate(urlBuilder.build().toString())

                page.waitForTimeout(EcommerceClientApiImpl.SCRAPER_PAGE_TIMEOUT_MILLIS)
                page.keyboard().down("End")
                extractContent(page.content(), searchData)
            })
    }

    private fun extractContent(
        availableContent: String,
        searchData: MutableList<CommonSearchResponse.Data>
    ) {
        htmlDocument(availableContent) {
            script {
                withAttribute = "type" to "application/ld+json"
                val json = findAll { removeScriptTag(this@findAll.html) }
                log.debug("available content = {}", json)

                Json.parseToJsonElement(json).jsonObject["itemListElement"]?.jsonArray.orEmpty()
                    .mapNotNull { it.getJsonObject("item") }
                    .forEach { productJson -> searchData.add(extractProductData(productJson)) }
            }
        }
    }

    private fun extractProductData(productJson: JsonObject): CommonSearchResponse.Data {
        val productId = productJson.getJsonValue("sku")
        val name = productJson.getJsonValue("name")
        val url = ecommerceSource.restfulHost + productJson.getJsonValue("url")
        val image = productJson.getJsonValue("image")

        val offers = productJson.getJsonObject("offers")
        val price = offers?.getJsonValue("price")?.currencyFormat()

        return CommonSearchResponse.Data(
            id = productId,
            name = name,
            price = price,
            url = url,
            imagesUrl = image,
        )
    }
}

suspend fun blibliSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val ecommerceEngine = parameter.ecommerceEngine ?: EcommerceEngine.RESTFUL
    val searchRequest = parameter.commonSearchRequest
    LoggerFactory.getLogger("BlibliClientEngine").debug("actual engine = {}", ecommerceEngine)
    return when (ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            BlibliClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            BlibliClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}
