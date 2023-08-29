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
        val storeAddressProvince: String?,
        val url: String,
        val videoUrl: String?,
        val images: Images,
    ) {
        data class Images(
            val largeUrls: List<String>,
            val smallUrls: List<String>
        )
    }

    data class Meta(
        val page: Int = -1,
        val perPage: Int? = -1,
        val total: Int? = -1,
        val totalPages: Int? = -1
    )
}
