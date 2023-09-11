package com.ramusthastudio.ecommerce.model

import kotlinx.serialization.Serializable

@Serializable
data class CommonSearchRequest(
    val page: String,
    val offset: String,
    val limit: String = "20",
    var xparam: MutableMap<String, String> = mutableMapOf(),
    val query: String,
    val engine: String?,
    val ecommerce: String?
)

fun commonSearchRequest(
    query: String = "",
    engine: String? = null,
    ecommerce: String? = null
): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        query = query,
        engine = engine,
        ecommerce = ecommerce,
    )
}
