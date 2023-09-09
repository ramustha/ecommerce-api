package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.common.asResource
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceEngine
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.commonSearchRequest
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EcommerceClientApiMockTest : AbstractEcommerceClientApiTest {

    override fun searchProductEmptyCombineTest() {}
    override fun searchProductNormalCombineTest() {}

    @Test
    override fun searchProductEmptyBlibliTest() {
        runBlocking {
            val mockEngine = engineMock("blibli-empty-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceSource.BLIBLI)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(1, searchProduct.meta.page)
            assertEquals(0, searchProduct.meta.perPage)
            assertEquals(0, searchProduct.meta.total)
            assertEquals(0, searchProduct.meta.totalPages)
            assertEquals("BLIBLI", searchProduct.meta.source)

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductEmptyBukalapakTest() {
        runBlocking {
            val mockEngine = engineMock("bukalapak-empty-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceSource.BUKALAPAK)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(1, searchProduct.meta.page)
            assertEquals(-1, searchProduct.meta.perPage)
            assertEquals(-1, searchProduct.meta.total)
            assertEquals(-1, searchProduct.meta.totalPages)
            assertEquals("BUKALAPAK", searchProduct.meta.source)

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductEmptyTokopediaTest() {
        runBlocking {
            val mockEngine = engineMock("tokopedia-empty-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceSource.TOKOPEDIA)

            assertEquals(emptyList<CommonSearchResponse.Data>(), searchProduct.data)
            assertEquals(-1, searchProduct.meta.page)
            assertEquals(-1, searchProduct.meta.perPage)
            assertEquals(0, searchProduct.meta.total)
            assertEquals(-1, searchProduct.meta.totalPages)
            assertEquals("TOKOPEDIA", searchProduct.meta.source)

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductNormalBlibliTest() {
        runBlocking {
            val mockEngine = engineMock("blibli-normal-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceSource.BLIBLI)
            val data = searchProduct.data
            data.forEach{ println(it)}
            assertEquals(36, searchProduct.data.size)

            val searchResultData = data.first()

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
            assertEquals(1, searchProduct.meta.page)
            assertEquals(40, searchProduct.meta.perPage)
            assertEquals(36, searchProduct.meta.total)
            assertEquals(1, searchProduct.meta.totalPages)
            assertEquals("BLIBLI", searchProduct.meta.source)

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductNormalBukalapakTest() {
        runBlocking {
            val mockEngine = engineMock("bukalapak-normal-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceSource.BUKALAPAK)
            val data = searchProduct.data
            data.forEach{ println(it)}
            assertEquals(11, searchProduct.data.size)

            val searchResultData = data.first()

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
            assertEquals(1, searchProduct.meta.page)
            assertEquals(50, searchProduct.meta.perPage)
            assertEquals(11, searchProduct.meta.total)
            assertEquals(1, searchProduct.meta.totalPages)
            assertEquals("BUKALAPAK", searchProduct.meta.source)

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductNormalTokopediaTest() {
        runBlocking {
            val mockEngine = engineMock("tokopedia-normal-response.json")
            val searchProduct = searchProductMock(mockEngine, EcommerceSource.TOKOPEDIA)
            val data = searchProduct.data
            data.forEach{ println(it)}
            assertEquals(60, searchProduct.data.size)

            val searchResultData = data.first()

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
            assertEquals(2506, searchProduct.meta.total)
            assertEquals("TOKOPEDIA", searchProduct.meta.source)

            mockEngine.close()
        }
    }

    @Test
    override fun searchProductNormalShopeeTest() {
        runBlocking {
            val mockContent = "scrape/shopee-response.html".asResource()
            val commonSearchRequest = commonSearchRequest(
                engine = EcommerceEngine.SCRAPER.toString()
            )
            val searchProduct = searchProductMock(
                EcommerceSource.SHOPEE, commonSearchRequest, mockContent
            )
            val data = searchProduct.data
            data.forEach{ println(it)}
            assertEquals(60, searchProduct.data.size)

            var searchResultData = data.first()

            assertEquals("17692676207", searchResultData.id)
            assertEquals(BigDecimal.valueOf(-1), searchResultData.price)
            assertEquals(BigDecimal.valueOf(170000), searchResultData.lowPrice)
            assertEquals(BigDecimal.valueOf(370000), searchResultData.highPrice)
            assertThat(
                searchResultData.name,
                containsString("GAME BATOCERA FULL GAME PS1-4")
            )
            assertThat(
                searchResultData.url,
                containsString("https://shopee.co.id/GAME-BATOCERA-FULL-GAME-PS1-4-i.99032190.17692676207")
            )
            assertEquals(-1, searchProduct.meta.total)
            assertEquals(EcommerceSource.SHOPEE.toString(), searchProduct.meta.source)

            searchResultData = searchProduct.data.first { it.id == "9156229116" }

            assertEquals("9156229116", searchResultData.id)
            assertEquals(BigDecimal.valueOf(1258562), searchResultData.price)
            assertEquals(BigDecimal.valueOf(-1), searchResultData.lowPrice)
            assertEquals(BigDecimal.valueOf(-1), searchResultData.highPrice)
            assertThat(
                searchResultData.name,
                containsString("Komputer motherboard For Asus ROG STRIX B250G GAMING")
            )
            assertThat(
                searchResultData.url,
                containsString("https://shopee.co.id/Komputer-motherboard-For-Asus-ROG-STRIX-B250G-GAMING")
            )
            assertEquals(-1, searchProduct.meta.total)
            assertEquals(EcommerceSource.SHOPEE.toString(), searchProduct.meta.source)
        }
    }

    @Test
    fun searchProductNormalBlibliScraperTest() {
        runBlocking {
            val mockContent = "scrape/blibli-response.html".asResource()
            val commonSearchRequest = commonSearchRequest(
                engine = EcommerceEngine.SCRAPER.toString()
            )
            val searchProduct = searchProductMock(
                EcommerceSource.BLIBLI, commonSearchRequest, mockContent
            )
            val data = searchProduct.data
            data.forEach{ println(it)}
            assertEquals(36, searchProduct.data.size)

            val searchResultData = data.first()

            assertEquals("TIS-70232-14582", searchResultData.id)
            assertEquals(BigDecimal.valueOf(605000), searchResultData.price)
            assertNull(searchResultData.lowPrice)
            assertNull(searchResultData.highPrice)
            assertThat(
                searchResultData.name,
                containsString("USB Flashdisk Batocera Flashdisk Full Games Flashdisk Batocera USB")
            )
            assertThat(
                searchResultData.url,
                containsString("/p/usb-flashdisk-batocera-flashdisk-full-games-flashdisk-batocera-usb/")
            )
            assertEquals(-1, searchProduct.meta.total)
            assertEquals(EcommerceSource.BLIBLI.toString(), searchProduct.meta.source)

        }
    }

    @Test
    fun searchProductNormalTokopediaScraperTest() {
        runBlocking {
            val mockContent = "scrape/tokopedia-response.html".asResource()
            val commonSearchRequest = commonSearchRequest(
                engine = EcommerceEngine.SCRAPER.toString()
            )
            val searchProduct = searchProductMock(
                EcommerceSource.TOKOPEDIA, commonSearchRequest, mockContent
            )
            val data = searchProduct.data
            data.forEach{ println(it)}
            assertEquals(83, searchProduct.data.size)

            val searchResultData = data.first()

            assertEquals("0000001TOKOPEDIA", searchResultData.id)
            assertEquals(BigDecimal.valueOf(97900), searchResultData.price)
            assertNull(searchResultData.lowPrice)
            assertNull(searchResultData.highPrice)
            assertThat(
                searchResultData.name,
                containsString("Flashdisk Batocera All in One Game Emulator - Game Konsol Retro - 16 GB")
            )
            assertThat(
                searchResultData.url,
                containsString("https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEH_KNHs1p")
            )
            assertEquals(-1, searchProduct.meta.total)
            assertEquals(EcommerceSource.TOKOPEDIA.toString(), searchProduct.meta.source)

        }
    }

    @Test
    fun searchProductNormalBukalapakScraperTest() {
        runBlocking {
            val mockContent = "scrape/bukalapak-response.html".asResource()
            val commonSearchRequest = commonSearchRequest(
                engine = EcommerceEngine.SCRAPER.toString()
            )
            val searchProduct = searchProductMock(
                EcommerceSource.BUKALAPAK, commonSearchRequest, mockContent
            )
            val data = searchProduct.data
            data.forEach{ println(it)}
            assertEquals(16, searchProduct.data.size)

            val searchResultData = data.first()

            assertEquals("0000001" + EcommerceSource.BUKALAPAK, searchResultData.id)
            assertEquals(BigDecimal.valueOf(199000), searchResultData.price)
            assertNull(searchResultData.lowPrice)
            assertNull(searchResultData.highPrice)
            assertThat(
                searchResultData.name,
                containsString("Batocera Full Games PS1 PS2 PS3 Emulator Portable Ubah PC")
            )
            assertThat(
                searchResultData.url,
                containsString("https://www.bukalapak.com/p/hobi-koleksi/video-game/game-watch-portable")
            )
            assertEquals(-1, searchProduct.meta.total)
            assertEquals(EcommerceSource.BUKALAPAK.toString(), searchProduct.meta.source)

        }
    }
}
