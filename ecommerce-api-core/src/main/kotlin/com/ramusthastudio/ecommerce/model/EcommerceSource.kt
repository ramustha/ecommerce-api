package com.ramusthastudio.ecommerce.model

enum class EcommerceSource(
    val host: String,
    val path: String
) {
    BLIBLI("www.blibli.com", "/backend/search/products"),
    BUKALAPAK_AUTH("www.bukalapak.com", "/westeros_auth_proxies"),
    BUKALAPAK("api.bukalapak.com", "/multistrategy-products"),
    TOKOPEDIA("gql.tokopedia.com", "/graphql/SearchProductQueryV4"),
    SHOPEE("shopee.co.id", "/v4/search/search_items")
}
