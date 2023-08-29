package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceHost
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EcommerceClientApiMockTest : AbstractEcommerceClientApiTest {

    @Test
    override fun searchProductEmptyBlibliTest() {
        runBlocking {
            val mockEngine = engineMock("blibli-empty-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceHost.BLIBLI)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(
                CommonSearchResponse.Meta(
                    page = 1,
                    perPage = 0,
                    total = 0,
                    totalPages = 0,
                ), searchProduct.meta
            )

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductEmptyBukalapakTest() {
        runBlocking {
            val mockEngine = engineMock("bukalapak-empty-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceHost.BUKALAPAK)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(
                CommonSearchResponse.Meta(
                    page = 1,
                    perPage = -1,
                    total = -1,
                    totalPages = -1,
                ), searchProduct.meta
            )

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductEmptyTokopediaTest() {
        runBlocking {
            val mockEngine = engineMock("tokopedia-empty-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceHost.TOKOPEDIA)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(
                CommonSearchResponse.Meta(
                    page = -1,
                    perPage = -1,
                    total = 0,
                    totalPages = -1,
                ), searchProduct.meta
            )

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductNormalBlibliTest() {
        runBlocking {
            val mockEngine = engineMock("blibli-normal-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceHost.BLIBLI)
            val searchResultData = searchProduct.data.first()

            assertEquals("SUP-25818-00923", searchResultData.id)
            assertEquals(BigDecimal.valueOf(200000), searchResultData.price)
            assertThat(
                searchResultData.name,
                containsString("USB Flashdisk Batocera Flashdisk Full Games Flashdisk")
            )
            assertThat(
                searchResultData.url,
                containsString("https://www.blibli.com/p/usb-flashdisk-batocera-flashdisk")
            )
            assertEquals(
                CommonSearchResponse.Meta(
                    page = 1,
                    perPage = 40,
                    total = 36,
                    totalPages = 1,
                ), searchProduct.meta
            )

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductNormalBukalapakTest() {
        runBlocking {
            val mockEngine = engineMock("bukalapak-normal-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceHost.BUKALAPAK)
            val searchResultData = searchProduct.data.first()

            assertEquals("4hj8vfm", searchResultData.id)
            assertEquals(BigDecimal.valueOf(199000), searchResultData.price)
            assertThat(
                searchResultData.name,
                containsString("Batocera Full Games PS1 PS2 PS3 Emulator Portable Ubah PC")
            )
            assertThat(
                searchResultData.url,
                containsString("https://www.bukalapak.com/p/hobi-koleksi/video-game/game-watch-portable/4hj8vfm")
            )
            assertEquals(
                CommonSearchResponse.Meta(
                    page = 1,
                    perPage = 50,
                    total = 11,
                    totalPages = 1,
                ), searchProduct.meta
            )

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductNormalTokopediaTest() {
        runBlocking {
            val mockEngine = engineMock("tokopedia-normal-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceHost.TOKOPEDIA)
            val searchResultData = searchProduct.data.first()

            assertEquals("8926635387", searchResultData.id)
            assertEquals(BigDecimal.valueOf(100000), searchResultData.price)
            assertThat(
                searchResultData.name,
                containsString("BATOCERA Flashdrive Hardisk For PC Laptop Plug & Play")
            )
            assertThat(
                searchResultData.url,
                containsString("https://www.tokopedia.com/mpgame/batocera-flashdrive-hardisk-for-pc-laptop-")
            )
            assertEquals(
                CommonSearchResponse.Meta(
                    total = 2506,
                ), searchProduct.meta
            )

            mockEngine.close()
        }
    }
}
