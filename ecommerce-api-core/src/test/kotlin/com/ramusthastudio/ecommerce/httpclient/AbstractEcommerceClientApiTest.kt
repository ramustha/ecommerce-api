package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.common.asResource
import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.commonSearchRequest
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

interface AbstractEcommerceClientApiTest {

    fun engineMock(
        responseString: String,
        httpStatusCode: HttpStatusCode = HttpStatusCode.OK,
        headers: Headers = headersOf(HttpHeaders.ContentType, "application/json")
    ) = MockEngine {
        respond(
            content = ByteReadChannel(responseString.asResource()),
            status = httpStatusCode,
            headers = headers
        )
    }

    suspend fun searchProductMock(
        engine: HttpClientEngine,
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest = commonSearchRequest()
    ): CommonSearchResponse {
        val ecommerceClientApi = EcommerceClientApiImpl(engine)
        return ecommerceClientApi.searchProduct(ecommerceSource)
    }

    suspend fun searchProductMock(
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest,
        content: String
    ): CommonSearchResponse {
        val ecommerceClientApi = EcommerceClientApiImpl()
        return ecommerceClientApi.searchProduct(ecommerceSource, commonSearchRequest, content)
    }

    fun searchProductEmptyBlibliTest()
    fun searchProductEmptyBukalapakTest()
    fun searchProductEmptyTokopediaTest()

    fun searchProductNormalBlibliTest()
    fun searchProductNormalBukalapakTest()
    fun searchProductNormalTokopediaTest()

    fun searchProductNormalShopeeTest()

    fun searchProductEmptyCombineTest()
    fun searchProductNormalCombineTest()
}
