package com.ramusthastudio.ecommerce.common

import com.ramusthastudio.ecommerce.model.CommonSearchRequest

fun commonSearchRequest(query: String): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        query = query
    )
}
