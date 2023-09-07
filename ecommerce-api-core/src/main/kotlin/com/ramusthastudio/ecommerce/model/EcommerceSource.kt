package com.ramusthastudio.ecommerce.model

enum class EcommerceSource(
    val restfulHost: String,
    val scraperHost: String,
    val restfulPath: String,
    val scraperPath: String
) {
    BLIBLI(
        "www.blibli.com",
        "https://www.blibli.com",
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
        "https://www.bukalapak.com",
        "/multistrategy-products",
        "/products"
    ),
    SHOPEE(
        "shopee.co.id",
        "https://shopee.co.id",
        "/v4/search/search_items",
        "/search"
    ),
    TOKOPEDIA(
        "gql.tokopedia.com",
        "https://www.tokopedia.com",
        "/graphql/SearchProductQueryV4",
        "/search"
    ),
}
