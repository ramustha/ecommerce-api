package com.ramusthastudio.ecommerce.model

data class CommonSearchRequest(
    val page: String,
    val offset: String,
    val limit: String = "20",
    var xparam: MutableMap<String, String> = mutableMapOf(),
    val query: String
)

fun commonSearchRequest(query: String = ""): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        query = query
    )
}
