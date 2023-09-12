package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.common.convertBukalapakSearchResponse
import com.ramusthastudio.ecommerce.common.currencyFormat
import com.ramusthastudio.ecommerce.model.BukalapakAuthRequest
import com.ramusthastudio.ecommerce.model.BukalapakAuthResponse
import com.ramusthastudio.ecommerce.model.BukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.SearchParameter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.set
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import java.util.*

private class BukalapakClientEngine(
    private val httpClient: HttpClient,
    private val browser: Browser,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val ecommerceSource = EcommerceSource.BUKALAPAK

    override suspend fun searchByRestful(): CommonSearchResponse = coroutineScope {
        val xparam = commonSearchRequest.xparam

        if (!httpClient.engine.toString().contains("MockEngine")) {
            val authRequest = EcommerceSource.BUKALAPAK_AUTH

            httpClient.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = authRequest.restfulHost

                    "common-headers".asResourceMap().map { header(it.key, it.value) }

                    path(authRequest.restfulPath)
                    contentType(ContentType.Application.Json)
                    setBody(BukalapakAuthRequest())
                }
            }.let { res -> xparam["access_token"] = res.body<BukalapakAuthResponse>().accessToken }
        }

        xparam["limit"] = "50"
        val searchResponse: HttpResponse = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ecommerceSource.restfulHost

                "common-headers".asResourceMap().map { header(it.key, it.value) }

                path(ecommerceSource.restfulPath)
                parameters.append("page", commonSearchRequest.page)
                parameters.append("offset", commonSearchRequest.offset)
                parameters.append("keywords", commonSearchRequest.query)
                xparam.forEach {
                    parameters.append(it.key, it.value)
                }
            }
        }
        xparam.remove("access_token")
        xparam.remove("limit")
        searchResponse.call

        val processTime = searchResponse.responseTime.timestamp - searchResponse.requestTime.timestamp
        log.debug("process time (RESTFUL)= $processTime")

        convertBukalapakSearchResponse(
            processTime,
            searchResponse.body<BukalapakSearchResponse>()
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

                    path(ecommerceSource.scraperPath)
                    parameters.append("search[keywords]", commonSearchRequest.query)
                    parameters.append("page", commonSearchRequest.page)
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
        val document = Jsoup.parse(availableContent)
        val productCards = document.getElementsByClass("te-product-card")
        productCards.forEachIndexed { index, data -> searchData.add(extractProductData(index, data)) }
    }

    private fun extractProductData(index: Int, element: Element): CommonSearchResponse.Data {
        val sliders = element.getElementsByClass("bl-thumbnail__slider")

        val name = element.getElementsByClass("bl-product-card-new__name").text()
        val prices = element.getElementsByClass("bl-product-card-new__prices").text()

        val sl = sliders.first()
        val url = sl.select("a").attr("href")
        val image = sl.select("img").attr("src")

        return CommonSearchResponse.Data(
            id = String.format("000000${index + 1}") + ecommerceSource.toString(),
            name = name,
            price = prices.currencyFormat(),
            url = url,
            imagesUrl = image,
        )
    }
}

suspend fun bukalapakSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val ecommerceEngine = parameter.ecommerceEngine ?: EcommerceEngine.RESTFUL
    val searchRequest = parameter.commonSearchRequest
    LoggerFactory.getLogger("BukalapakClientEngine").debug("actual engine = {}", ecommerceEngine)
    return when (ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            BukalapakClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            BukalapakClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}
