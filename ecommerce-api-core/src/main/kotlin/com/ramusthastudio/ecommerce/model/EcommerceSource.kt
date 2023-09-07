package com.ramusthastudio.ecommerce.model

enum class EcommerceSource(
    val restfulHost: String,
    val scraperHost: String,
    val restfulPath: String,
    val scraperPath: String
) {
    BLIBLI(
        "www.blibli.com",
        "www.blibli.com",
        "/backend/search/products",
        "/cari"
    ),
    BUKALAPAK_AUTH(
        "www.bukalapak.com",
        "https://",
        "/westeros_auth_proxies",
        "/"
    ),
    BUKALAPAK(
        "api.bukalapak.com",
        "www.bukalapak.com",
        "/multistrategy-products",
        "/products"
    ),
    SHOPEE(
        "shopee.co.id",
        "shopee.co.id",
        "/v4/search/search_items",
        "/search"
    ),
    TOKOPEDIA(
        "gql.tokopedia.com",
        "www.tokopedia.com",
        "/graphql/SearchProductQueryV4",
        "/search"
    ),
}
