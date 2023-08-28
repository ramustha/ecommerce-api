package com.ramusthastudio.ecommerce.model

import io.ktor.http.URLProtocol

enum class EcommerceHost(
    val protocol: URLProtocol,
    val host: String,
    val path: String
) {
    BLIBLI(URLProtocol.HTTPS, "www.blibli.com", "/backend/search/products"),
    BUKALAPAK_AUTH(URLProtocol.HTTPS, "www.bukalapak.com", "/westeros_auth_proxies"),
    BUKALAPAK(URLProtocol.HTTPS, "api.bukalapak.com", "/multistrategy-products"),
    TOKOPEDIA(URLProtocol.HTTPS, "gql.tokopedia.com", "/graphql/SearchProductQueryV4"),
    SHOPEE(URLProtocol.HTTPS, "shopee.co.id", "/v4/search/search_items")
}
