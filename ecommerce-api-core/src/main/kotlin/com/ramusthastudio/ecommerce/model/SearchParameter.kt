package com.ramusthastudio.ecommerce.model

class SearchParameter(
    val commonSearchRequest: CommonSearchRequest = commonSearchRequest(),
    val ecommerceEngine: EcommerceEngine? = null,
    val content: String? = null
)
