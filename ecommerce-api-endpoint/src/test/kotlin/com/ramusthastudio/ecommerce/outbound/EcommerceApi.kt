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
    fun `Search Product Test`() = testApplication {
        val response = client.get("/api/search?q=batocera")
        assertNotNull(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }

}
