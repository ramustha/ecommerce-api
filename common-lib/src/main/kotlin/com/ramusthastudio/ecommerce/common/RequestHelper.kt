package com.ramusthastudio.ecommerce.common

import com.ramusthastudio.ecommerce.model.CommonSearchRequest

fun commonSearchRequest(query: String): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        xparam = mutableMapOf(),
        query = query
    )
}

fun blibliSearchRequest(query: String): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        xparam = mutableMapOf("channelId" to "web"),
        query = query
    )
}

fun bukalapakSearchRequest(query: String): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        xparam = mutableMapOf("limit" to "50"),
        query = query
    )
}
