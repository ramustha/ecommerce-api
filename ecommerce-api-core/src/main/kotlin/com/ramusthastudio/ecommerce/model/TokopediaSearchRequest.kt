package com.ramusthastudio.ecommerce.model

import com.ramusthastudio.ecommerce.common.asResource
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TokopediaSearchRequest(
    val operationName: String,
    val variables: Variables,
    val query: String
) {
    @Serializable
    data class Variables(
        val params: String
    )
}

fun constructTokopediaBody(query: String): TokopediaSearchRequest {
    return TokopediaSearchRequest(
        operationName = "SearchProductQueryV4",
        variables = TokopediaSearchRequest.Variables(constructTokopediaParams(query)),
        query = "tokopedia-request.graphql".asResource()
    )
}

private fun constructTokopediaParams(query: String): String {
    val builder: Map<String, String> =
        mapOf(
            "device" to "desktop",
            "navsource" to "",
            "ob" to "23",
            "page" to "1",
            "q" to query,
            "related" to "true",
            "rows" to "60",
            "safe_search" to "false",
            "scheme" to "https",
            "shipping" to "",
            "show_adult" to "false",
            "source" to "search",
            "srp_component_id" to "02.01.00.00",
            "srp_page_id" to "",
            "srp_page_title" to "",
            "st" to "product",
            "start" to "0",
            "topads_bucket" to "true",
            "unique_id" to UUID.randomUUID().toString().replace("-", ""),
            "user_addressId" to "",
            "user_cityId" to "",
            "user_districtId" to "",
            "user_id" to "",
            "user_lat" to "",
            "user_long" to "",
            "user_postCode" to "",
            "user_warehouseId" to "",
            "variants" to "",
            "warehouses" to "",
        )
    return builder.entries.stream()
        .map { it.key + "=" + it.value }
        .toList()
        .joinToString("&")
}
