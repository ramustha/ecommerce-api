package com.ramusthastudio.ecommerce.common

import com.ramusthastudio.ecommerce.model.CommonSearchRequest

fun blibliCommonRequest(query: String): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        xparam = mutableMapOf("channelId" to "web"),
        query = query
    )
}

fun bukalapakCommonRequest(query: String): CommonSearchRequest {
    return CommonSearchRequest(
        page = "1",
        offset = "0",
        xparam = mutableMapOf("limit" to "50"),
        query = query
    )
}
