package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.commonSearchRequest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.time.Duration.Companion.seconds

class EcommerceClientApiTest : AbstractEcommerceClientApiTest {
    private val ecommerceClientApi = EcommerceClientApiImpl()

    @AfterEach
    fun tearDown() {
        ecommerceClientApi.close()
    }

    @Test
    fun bukalapakAuthTest() = runTest {
        val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.BUKALAPAK_AUTH)

        assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
        assertEquals(CommonSearchResponse.Meta(), searchProduct.meta)
    }

    @Test
    override fun searchProductEmptyBlibliTest() = runTest {
        val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.BLIBLI)

        assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
        assertEquals(1, searchProduct.meta.page)
        assertEquals(0, searchProduct.meta.perPage)
        assertEquals(0, searchProduct.meta.total)
        assertEquals(0, searchProduct.meta.totalPages)
        assertEquals("BLIBLI", searchProduct.meta.source)
    }

    @Test
    override fun searchProductEmptyBukalapakTest() = runTest {
        val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.BUKALAPAK)

        assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
        assertEquals(1, searchProduct.meta.page)
        assertEquals(-1, searchProduct.meta.perPage)
        assertEquals(-1, searchProduct.meta.total)
        assertEquals(-1, searchProduct.meta.totalPages)
        assertEquals("BUKALAPAK", searchProduct.meta.source)
    }

    @Test
    override fun searchProductEmptyTokopediaTest() = runTest {
        val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.TOKOPEDIA)

        assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
        assertEquals(-1, searchProduct.meta.page)
        assertEquals(-1, searchProduct.meta.perPage)
        assertEquals(0, searchProduct.meta.total)
        assertEquals(-1, searchProduct.meta.totalPages)
        assertEquals("TOKOPEDIA", searchProduct.meta.source)
    }

    @Test
    override fun searchProductNormalBlibliTest() = runTest {
        val searchProduct = ecommerceClientApi.searchProduct(
            EcommerceSource.BLIBLI, commonSearchRequest("batocera")
        )
        val data = searchProduct.data
        val productMeta = searchProduct.meta

        data.forEach { println(it) }
        val searchResultData = data.first()

        assertNotNull(searchResultData.id)
        assertNotNull(searchResultData.name)
        assertNotNull(searchResultData.price)
        assertNotNull(searchResultData.originalPrice)
        assertNotNull(searchResultData.storeName)
        assertNotNull(searchResultData.storeAddressCity)
        assertNotNull(searchResultData.url)
        assertNotNull(searchResultData.imagesUrl)
        assertNotEquals(-1, productMeta.page)
        assertNotEquals(-1, productMeta.perPage)
        assertNotEquals(-1, productMeta.total)
        assertNotEquals(-1, productMeta.totalPages)
    }

    @Test
    override fun searchProductNormalBukalapakTest() = runTest {
        val searchProduct = ecommerceClientApi.searchProduct(
            EcommerceSource.BUKALAPAK, commonSearchRequest("batocera")
        )
        val data = searchProduct.data
        val productMeta = searchProduct.meta

        data.forEach { println(it) }
        val searchResultData = data.first()

        assertNotNull(searchResultData.id)
        assertNotNull(searchResultData.name)
        assertNotNull(searchResultData.price)
        assertNotNull(searchResultData.originalPrice)
        assertNotNull(searchResultData.storeName)
        assertNotNull(searchResultData.storeAddressCity)
        assertNotNull(searchResultData.url)
        assertNotNull(searchResultData.imagesUrl)
        assertNotEquals(-1, productMeta.page)
        assertNotEquals(-1, productMeta.perPage)
        assertNotEquals(-1, productMeta.total)
        assertNotEquals(-1, productMeta.totalPages)
    }

    @Test
    override fun searchProductNormalTokopediaTest() = runTest {
        val searchProduct = ecommerceClientApi.searchProduct(
            EcommerceSource.TOKOPEDIA, commonSearchRequest("batocera")
        )
        val data = searchProduct.data
        val productMeta = searchProduct.meta

        data.forEach { println(it) }
        val searchResultData = data.first()

        assertNotNull(searchResultData.id)
        assertNotNull(searchResultData.name)
        assertNotNull(searchResultData.price)
        assertNotNull(searchResultData.originalPrice)
        assertNotNull(searchResultData.storeName)
        assertNotNull(searchResultData.storeAddressCity)
        assertNotNull(searchResultData.url)
        assertNotNull(searchResultData.imagesUrl)
        assertNotEquals(-1, productMeta.total)
    }

    @Test
    override fun searchProductNormalShopeeTest() = runTest(
        timeout = 20.seconds
    ) {
        val ecommerceSource = EcommerceSource.SHOPEE
        val searchProduct = ecommerceClientApi.searchProduct(
            ecommerceSource, commonSearchRequest("batocera")
        )
        val data = searchProduct.data
        val productMeta = searchProduct.meta

        data.forEach { println(it) }
        val searchResultData = data.first()

        assertNotEquals(0, searchProduct.data.size)
        assertNotNull(searchResultData.id)
        assertNotNull(searchResultData.name)
        assertNotNull(searchResultData.url)
        assertNotNull(searchResultData.imagesUrl)
        assertEquals(ecommerceSource.toString(), productMeta.source)
        assertNotEquals(-1, productMeta.processTime)
    }

    @Test
    override fun searchProductEmptyCombineTest() = runTest(
        timeout = 20.seconds
    )  {
        val searchProductList = ecommerceClientApi.searchProductCombine()
        assertEquals(4, searchProductList.size)
    }

    @Test
    override fun searchProductNormalCombineTest() = runTest(
        timeout = 20.seconds
    )  {
        val searchProductList = ecommerceClientApi.searchProductCombine(
            commonSearchRequest("batocera")
        )
        assertEquals(4, searchProductList.size)
    }
}
