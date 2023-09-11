package com.ramusthastudio.ecommerce.model

import com.ramusthastudio.ecommerce.common.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CommonSearchResponse(
    val data: List<Data> = emptyList(),
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        val id: String,
        val name: String,
        @Serializable(with = BigDecimalSerializer::class)
        var price: BigDecimal? = null,
        @Serializable(with = BigDecimalSerializer::class)
        var originalPrice: BigDecimal? = null,
        @Serializable(with = BigDecimalSerializer::class)
        var lowPrice: BigDecimal? = null,
        @Serializable(with = BigDecimalSerializer::class)
        var highPrice: BigDecimal? = null,
        var storeName: String? = null,
        var storeAddressCity: String? = null,
        val url: String,
        val imagesUrl: String,
    )

    @Serializable
    data class Meta(
        val page: Int = -1,
        val perPage: Int? = -1,
        val total: Int? = -1,
        val totalPages: Int? = -1,
        val source: String? = null,
        val processTime: Long = -1
    )
}
