package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceHost
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class EcommerceClientApiTest : AbstractEcommerceClientApiTest {
    private val ecommerceClientApi = EcommerceClientApiImpl()

    @AfterEach
    fun tearDown() {
        ecommerceClientApi.close()
    }

    @Test
    fun bukalapakAuthTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceHost.BUKALAPAK_AUTH)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(CommonSearchResponse.Meta(), searchProduct.meta)
        }
    }

    @Test
    override fun searchProductEmptyBlibliTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceHost.BLIBLI)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(
                CommonSearchResponse.Meta(
                    page = 1,
                    perPage = 0,
                    total = 0,
                    totalPages = 0,
                ), searchProduct.meta
            )
        }
    }

    @Test
    override fun searchProductEmptyBukalapakTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceHost.BUKALAPAK)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(
                CommonSearchResponse.Meta(
                    page = 1,
                    perPage = -1,
                    total = -1,
                    totalPages = -1,
                ), searchProduct.meta
            )
        }
    }

    @Test
    override fun searchProductEmptyTokopediaTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(EcommerceHost.TOKOPEDIA)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(
                CommonSearchResponse.Meta(
                    page = -1,
                    perPage = -1,
                    total = 0,
                    totalPages = -1,
                ), searchProduct.meta
            )
        }
    }

    @Test
    override fun searchProductNormalBlibliTest() {
        runBlocking {
            val searchProduct = ecommerceClientApi.searchProduct(
                EcommerceHost.BLIBLI, "batocera"
            )

            val productData = searchProduct.data.first()
            val productMeta = searchProduct.meta

            assertNotNull(productData.id)
            assertNotNull(productData.name)
            assertNotNull(productData.description)
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
                EcommerceHost.BUKALAPAK, "batocera"
            )

            val productData = searchProduct.data.first()
            val productMeta = searchProduct.meta

            assertNotNull(productData.id)
            assertNotNull(productData.name)
            assertNotNull(productData.description)
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
                EcommerceHost.TOKOPEDIA, "batocera"
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
}
