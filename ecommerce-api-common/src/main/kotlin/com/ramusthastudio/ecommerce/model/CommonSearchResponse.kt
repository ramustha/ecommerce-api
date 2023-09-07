package com.ramusthastudio.ecommerce.model

import java.math.BigDecimal

data class CommonSearchResponse(
    val data: List<Data> = emptyList(),
    val meta: Meta = Meta()
) {
    data class Data(
        val id: String,
        val name: String,
        var price: BigDecimal? = null,
        var originalPrice: BigDecimal? = null,
        var lowPrice: BigDecimal? = null,
        var highPrice: BigDecimal? = null,
        var storeName: String? = null,
        var storeAddressCity: String? = null,
        val url: String,
        val imagesUrl: String,
    )

    data class Meta(
        val page: Int = -1,
        val perPage: Int? = -1,
        val total: Int? = -1,
        val totalPages: Int? = -1,
        val source: String? = null,
        val processTime: Long = -1
    )
}
