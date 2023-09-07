package com.ramusthastudio.ecommerce.httpclient

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.WaitUntilState
import com.ramusthastudio.ecommerce.common.asResourceMap
import com.ramusthastudio.ecommerce.common.convertBlibliSearchResponse
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
import io.ktor.http.URLProtocol
import io.ktor.http.path
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.script
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory


class BlibliClientEngine(
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

        val page: Page = browser.newPage()
        val navigateOptions = Page.NavigateOptions()
        navigateOptions.setWaitUntil(WaitUntilState.LOAD)

        page.navigate("https://www.blibli.com/cari/batocera?page=1&start=0&sort=0", navigateOptions)
        page.keyboard().down("End")

        val searchData = mutableListOf<CommonSearchResponse.Data>()
        htmlDocument(page.content()) {
            script {
                withAttribute = "type" to "application/ld+json"
                val json = findFirst { removeScriptTag(this@findFirst.html) }
                val jsonElement = Json.parseToJsonElement(json)
            }
        }

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
}

suspend fun blibliSearch(
    httpClient: HttpClient,
    browser: Browser,
    searchParameter: () -> SearchParameter
): CommonSearchResponse {
    val parameter = searchParameter()
    val ecommerceEngine = parameter.ecommerceEngine ?: EcommerceEngine.RESTFUL
    val searchRequest = parameter.commonSearchRequest
    return when (ecommerceEngine) {
        EcommerceEngine.RESTFUL ->
            BlibliClientEngine(httpClient, browser, searchRequest).searchByRestful()

        EcommerceEngine.SCRAPER ->
            BlibliClientEngine(httpClient, browser, searchRequest).searchByScraper(parameter.content)
    }
}
