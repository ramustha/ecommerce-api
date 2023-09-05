package com.ramusthastudio.ecommerce.model

class SearchParameter(
    val commonSearchRequest: CommonSearchRequest = commonSearchRequest(),
    val ecommerceEngine: EcommerceEngine,
    val content: String? = null
)
