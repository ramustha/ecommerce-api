package com.ramusthastudio.ecommerce

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay

suspend fun main() {
    val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    val response: HttpResponse = client.get("https://ktor.io/")
    println(response.status)
    client.close()

    delay(2000)
}
