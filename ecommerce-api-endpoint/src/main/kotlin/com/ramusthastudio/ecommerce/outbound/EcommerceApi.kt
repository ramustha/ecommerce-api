package com.ramusthastudio.ecommerce.outbound

import com.ramusthastudio.ecommerce.httpclient.EcommerceClientApiImpl
import com.ramusthastudio.ecommerce.model.commonSearchRequest
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
}

fun Application.configureRouting() {
    routing {
        ecommerceRouting()
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
}

val ecommerceClientApiImpl = EcommerceClientApiImpl()

fun Route.ecommerceRouting() {
    route("/") {
        get("/api/search") {
            val query = call.request.queryParameters["q"] ?: return@get call.respondText(
                "Missing query",
                status = HttpStatusCode.BadRequest
            )
            call.respond(ecommerceClientApiImpl.searchProductCombine(commonSearchRequest(query = query)))
        }
    }
}
