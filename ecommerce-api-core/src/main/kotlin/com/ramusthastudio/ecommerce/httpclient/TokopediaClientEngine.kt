package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.LoadState
import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.common.convertTokopediaSearchResponse
import com.ramusthastudio.ecommerce.common.currencyFormat
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.SearchParameter
import com.ramusthastudio.ecommerce.model.TokopediaSearchResponse
import com.ramusthastudio.ecommerce.model.constructTokopediaBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
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

private class TokopediaClientEngine(
    private val httpClient: HttpClient,
    private val browser: Browser,
    private val commonSearchRequest: CommonSearchRequest
) : ClientEngine {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val ecommerceSource = EcommerceSource.TOKOPEDIA

    override suspend fun searchByRestful(): CommonSearchResponse = coroutineScope {
        val searchResponse: HttpResponse = httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = ecommerceSource.restfulHost

                "common-headers".asResourceMap().map { header(it.key, it.value) }

                path(ecommerceSource.restfulPath)
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Referrer, "https://www.tokopedia.com")
                setBody(listOf(constructTokopediaBody(commonSearchRequest.query)))
            }
        }

        val processTime = searchResponse.responseTime.timestamp - searchResponse.requestTime.timestamp
        log.debug("process time (RESTFUL)= $processTime")

        convertTokopediaSearchResponse(
            processTime = processTime,
            searchResponse.body<List<TokopediaSearchResponse>>().first()
        )
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

                val urlBuilder = URLBuilder()
                urlBuilder.set {
                    protocol = URLProtocol.HTTPS
                    host = ecommerceSource.scraperHost

                    path(ecommerceSource.scraperPath)
                    parameters.append("page", commonSearchRequest.page)
                    parameters.append("q", commonSearchRequest.query)
                }
                val page: Page = browser.newPage()
                page.navigate(urlBuilder.build().toString())

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
        val document = Jsoup.parse(availableContent)
        val productCards = document.getElementsByClass("prd_container-card")
        productCards.forEachIndexed { index, data -> searchData.add(extractProductData(index, data)) }
    }

    private fun extractProductData(index: Int, element: Element): CommonSearchResponse.Data {
        val name = element.getElementsByClass("prd_link-product-name").text()
        val price = element.getElementsByClass("prd_link-product-price").text()

        val url = element.select("a").attr("href")
        val image = element.select("img").attr("src")

        return CommonSearchResponse.Data(
            id = String.format("000000${index + 1}") + ecommerceSource.toString(),
            name = name,
            price = price.currencyFormat(),
            url = url,
            imagesUrl = image,
        )
    }
}

suspend fun tokopediaSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val ecommerceEngine = parameter.ecommerceEngine ?: EcommerceEngine.RESTFUL
    val searchRequest = parameter.commonSearchRequest
    return when (ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            TokopediaClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            TokopediaClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}
