package com.ramusthastudio.ecommerce.model

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
        query = constructTokopediaQuery()
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

private fun constructTokopediaQuery(): String {
    return "query SearchProductQueryV4(\$params: String!) {\n  ace_search_product_v4(params: \$params) {\n    header {\n      totalData\n      totalDataText\n      processTime\n      responseCode\n      errorMessage\n      additionalParams\n      keywordProcess\n      componentId\n      __typename\n    }\n    data {\n      banner {\n        position\n        text\n        imageUrl\n        url\n        componentId\n        trackingOption\n        __typename\n      }\n      backendFilters\n      isQuerySafe\n      ticker {\n        text\n        query\n        typeId\n        componentId\n        trackingOption\n        __typename\n      }\n      redirection {\n        redirectUrl\n        departmentId\n        __typename\n      }\n      related {\n        position\n        trackingOption\n        relatedKeyword\n        otherRelated {\n          keyword\n          url\n          product {\n            id\n            name\n            price\n            imageUrl\n            rating\n            countReview\n            url\n            priceStr\n            wishlist\n            shop {\n              city\n              isOfficial\n              isPowerBadge\n              __typename\n            }\n            ads {\n              adsId: id\n              productClickUrl\n              productWishlistUrl\n              shopClickUrl\n              productViewUrl\n              __typename\n            }\n            badges {\n              title\n              imageUrl\n              show\n              __typename\n            }\n            ratingAverage\n            labelGroups {\n              position\n              type\n              title\n              url\n              __typename\n            }\n            componentId\n            __typename\n          }\n          componentId\n          __typename\n        }\n        __typename\n      }\n      suggestion {\n        currentKeyword\n        suggestion\n        suggestionCount\n        instead\n        insteadCount\n        query\n        text\n        componentId\n        trackingOption\n        __typename\n      }\n      products {\n        id\n        name\n        ads {\n          adsId: id\n          productClickUrl\n          productWishlistUrl\n          productViewUrl\n          __typename\n        }\n        badges {\n          title\n          imageUrl\n          show\n          __typename\n        }\n        category: departmentId\n        categoryBreadcrumb\n        categoryId\n        categoryName\n        countReview\n        customVideoURL\n        discountPercentage\n        gaKey\n        imageUrl\n        labelGroups {\n          position\n          title\n          type\n          url\n          __typename\n        }\n        originalPrice\n        price\n        priceRange\n        rating\n        ratingAverage\n        shop {\n          shopId: id\n          name\n          url\n          city\n          isOfficial\n          isPowerBadge\n          __typename\n        }\n        url\n        wishlist\n        sourceEngine: source_engine\n        __typename\n      }\n      violation {\n        headerText\n        descriptionText\n        imageURL\n        ctaURL\n        ctaApplink\n        buttonText\n        buttonType\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n}\n"
}
