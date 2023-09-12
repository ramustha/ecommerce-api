package com.ramusthastudio.ecommerce.outbound

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertNotNull

class EcommerceApi {

    @Test
    fun `Search Product with missing query`() = testApplication {
        val response = client.get("/api/search")
        assertEquals("Missing query", response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Search Product Combine Test`() = testApplication {
        val response = client.get("/api/search?q=batocera")
        assertNotNull(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `Search Product with Source Test`() = testApplication {
        val response = client.get("/api/search?q=batocera&source=BUKALAPAK")
        assertNotNull(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `Search Product with Source and Engine Test`() = testApplication {
        val response = client.get("/api/search?q=batocera&source=BLIBLI&engine=SCRAPER")
        assertNotNull(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
