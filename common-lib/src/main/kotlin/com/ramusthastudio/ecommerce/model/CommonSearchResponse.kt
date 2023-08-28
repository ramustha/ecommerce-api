package com.ramusthastudio.ecommerce.model

import java.math.BigDecimal

data class CommonSearchResponse(
    val data: List<Data>,
    val meta: Meta
) {
    data class Data(
        val id: String,
        val name: String,
        val condition: String?,
        val active: Boolean = true,
        val description: String,
        val price: BigDecimal,
        val originalPrice: BigDecimal,
        val storeAddressCity: String,
        val storeAddressProvince: String,
        val url: String,
        val videoUrl: String?,
        val images: Images,
    ) {
        data class Images(
            val largeUrls: List<String?>?,
            val smallUrls: List<String?>?
        )
    }

    data class Meta(
        val page: Int,
        val perPage: Int,
        val total: Int,
        val totalPages: Int
    )
}
