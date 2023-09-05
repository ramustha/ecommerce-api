package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.commonSearchRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.system.measureTimeMillis

class EcommerceClientApiTest : AbstractEcommerceClientApiTest {
    private val ecommerceClientApi = EcommerceClientApiImpl()

    @AfterEach
    fun tearDown() {
        ecommerceClientApi.close()
    }

    @Test
    fun bukalapakAuthTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.BUKALAPAK_AUTH)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(CommonSearchResponse.Meta(), searchProduct.meta)
        }
    }

    @Test
    override fun searchProductEmptyBlibliTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.BLIBLI)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(1, searchProduct.meta.page)
            assertEquals(0, searchProduct.meta.perPage)
            assertEquals(0, searchProduct.meta.total)
            assertEquals(0, searchProduct.meta.totalPages)
            assertEquals("BLIBLI", searchProduct.meta.source)
        }
    }

    @Test
    override fun searchProductEmptyBukalapakTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.BUKALAPAK)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(1, searchProduct.meta.page)
            assertEquals(-1, searchProduct.meta.perPage)
            assertEquals(-1, searchProduct.meta.total)
            assertEquals(-1, searchProduct.meta.totalPages)
            assertEquals("BUKALAPAK", searchProduct.meta.source)
        }
    }

    @Test
    override fun searchProductEmptyTokopediaTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceSource.TOKOPEDIA)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(-1, searchProduct.meta.page)
            assertEquals(-1, searchProduct.meta.perPage)
            assertEquals(0, searchProduct.meta.total)
            assertEquals(-1, searchProduct.meta.totalPages)
            assertEquals("TOKOPEDIA", searchProduct.meta.source)
        }
    }

    @Test
    override fun searchProductNormalBlibliTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(
                EcommerceSource.BLIBLI, commonSearchRequest("batocera")
            )

            val productData = searchProduct.data.first()
            val productMeta = searchProduct.meta

            assertNotNull(productData.id)
            assertNotNull(productData.name)
            assertNotNull(productData.price)
            assertNotNull(productData.originalPrice)
            assertNotNull(productData.storeName)
            assertNotNull(productData.storeAddressCity)
            assertNotNull(productData.url)
            assertNotNull(productData.imagesUrl)
            assertNotEquals(-1, productMeta.page)
            assertNotEquals(-1, productMeta.perPage)
            assertNotEquals(-1, productMeta.total)
            assertNotEquals(-1, productMeta.totalPages)
        }
    }

    @Test
    override fun searchProductNormalBukalapakTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(
                EcommerceSource.BUKALAPAK, commonSearchRequest("batocera")
            )

            val productData = searchProduct.data.first()
            val productMeta = searchProduct.meta

            assertNotNull(productData.id)
            assertNotNull(productData.name)
            assertNotNull(productData.price)
            assertNotNull(productData.originalPrice)
            assertNotNull(productData.storeName)
            assertNotNull(productData.storeAddressCity)
            assertNotNull(productData.url)
            assertNotNull(productData.imagesUrl)
            assertNotEquals(-1, productMeta.page)
            assertNotEquals(-1, productMeta.perPage)
            assertNotEquals(-1, productMeta.total)
            assertNotEquals(-1, productMeta.totalPages)
        }
    }

    @Test
    override fun searchProductNormalTokopediaTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(
                EcommerceSource.TOKOPEDIA, commonSearchRequest("batocera")
            )

            val productData = searchProduct.data.first()
            val productMeta = searchProduct.meta

            assertNotNull(productData.id)
            assertNotNull(productData.name)
            assertNotNull(productData.price)
            assertNotNull(productData.originalPrice)
            assertNotNull(productData.storeName)
            assertNotNull(productData.storeAddressCity)
            assertNotNull(productData.url)
            assertNotNull(productData.imagesUrl)
            assertNotEquals(-1, productMeta.total)
        }
    }

    @Test
    override fun searchProductNormalShopeeTest() {
        runBlocking {
            val ecommerceSource = EcommerceSource.SHOPEE
            val searchProduct = ecommerceClientApi.searchProduct(
                ecommerceSource, commonSearchRequest("batocera")
            )

            val productData = searchProduct.data.first()
            val productMeta = searchProduct.meta
            println("response time = ${Duration.ofMillis(productMeta.responseTime)}")

            assertNotEquals(0, searchProduct.data.size)
            assertNotNull(productData.id)
            assertNotNull(productData.name)
            assertNotNull(productData.url)
            assertNotNull(productData.imagesUrl)
            assertEquals(ecommerceSource.toString(), productMeta.source)
            assertNotEquals(-1, productMeta.priority)
            assertNotEquals(-1, productMeta.responseTime)
        }
    }

    @Test
    override fun searchProductEmptyCombineTest() {
        runBlocking {
            val time = measureTimeMillis {
                val searchProductList = ecommerceClientApi.searchProductCombine()
                assertEquals(4, searchProductList.size)
            }
            println("time = $time")
        }
    }

    @Test
    override fun searchProductNormalCombineTest() {
        runBlocking {
            val time = measureTimeMillis {
                val searchProductList = ecommerceClientApi.searchProductCombine(
                    commonSearchRequest("batocera")
                )
                assertEquals(4, searchProductList.size)
            }
            println("time = $time")
        }
    }
}
