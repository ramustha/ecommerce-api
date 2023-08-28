package com.ramusthastudio.ecommerce.model

data class CommonSearchRequest(
    val page: String, //
    val offset: String,
    val limit: String = "20",
    var xparam: MutableMap<String, String>,
    val query: String, // searchTerm, keywords
)
