package com.ramusthastudio.ecommerce.model

import java.math.BigDecimal

data class CommonSearchResponse(
    val data: List<Data> = emptyList(),
    val meta: Meta = Meta()
) {
    data class Data(
        val id: String,
        val name: String,
        val condition: String?,
        val active: Boolean = true,
        val description: String?,
        val price: BigDecimal,
        val originalPrice: BigDecimal,
        val storeName: String?,
        val storeAddressCity: String,
        val url: String,
        val imagesUrl: String,
    )

    data class Meta(
        val page: Int = -1,
        val perPage: Int? = -1,
        val total: Int? = -1,
        val totalPages: Int? = -1
    )
}
