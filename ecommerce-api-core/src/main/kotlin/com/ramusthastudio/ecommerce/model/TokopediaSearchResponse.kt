package com.ramusthastudio.ecommerce.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TokopediaSearchResponse(
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("ace_search_product_v4")
        val aceSearchProductV4: AceSearchProductV4
    ) {
        @Serializable
        data class AceSearchProductV4(
            @SerialName("header")
            val header: Header,
            @SerialName("data")
            val data: Data,
            @SerialName("__typename")
            val typename: String?
        ) {
            @Serializable
            data class Header(
                @SerialName("totalData")
                val totalData: Int,
                @SerialName("totalDataText")
                val totalDataText: String?,
                @SerialName("processTime")
                val processTime: Double?,
                @SerialName("responseCode")
                val responseCode: Int?,
                @SerialName("errorMessage")
                val errorMessage: String?,
                @SerialName("additionalParams")
                val additionalParams: String?,
                @SerialName("keywordProcess")
                val keywordProcess: String?,
                @SerialName("componentId")
                val componentId: String?,
                @SerialName("__typename")
                val typename: String?
            )

            @Serializable
            data class Data(
                @SerialName("banner")
                val banner: Banner?,
                @SerialName("backendFilters")
                val backendFilters: String?,
                @SerialName("isQuerySafe")
                val isQuerySafe: Boolean?,
                @SerialName("ticker")
                val ticker: Ticker?,
                @SerialName("redirection")
                val redirection: Redirection?,
                @SerialName("related")
                val related: Related?,
                @SerialName("suggestion")
                val suggestion: Suggestion?,
                @SerialName("products")
                val products: List<Product>,
                @SerialName("violation")
                val violation: Violation?,
                @SerialName("__typename")
                val typename: String?
            ) {
                @Serializable
                data class Banner(
                    @SerialName("position")
                    val position: Int?,
                    @SerialName("text")
                    val text: String?,
                    @SerialName("imageUrl")
                    val imageUrl: String?,
                    @SerialName("url")
                    val url: String?,
                    @SerialName("componentId")
                    val componentId: String?,
                    @SerialName("trackingOption")
                    val trackingOption: Int?,
                    @SerialName("__typename")
                    val typename: String?
                )

                @Serializable
                data class Ticker(
                    @SerialName("text")
                    val text: String?,
                    @SerialName("query")
                    val query: String?,
                    @SerialName("typeId")
                    val typeId: Int?,
                    @SerialName("componentId")
                    val componentId: String?,
                    @SerialName("trackingOption")
                    val trackingOption: Int?,
                    @SerialName("__typename")
                    val typename: String?
                )

                @Serializable
                data class Redirection(
                    @SerialName("redirectUrl")
                    val redirectUrl: String?,
                    @SerialName("departmentId")
                    val departmentId: Int?,
                    @SerialName("__typename")
                    val typename: String?
                )

                @Serializable
                data class Related(
                    @SerialName("position")
                    val position: Int?,
                    @SerialName("trackingOption")
                    val trackingOption: Int?,
                    @SerialName("relatedKeyword")
                    val relatedKeyword: String?,
                    @SerialName("otherRelated")
                    val otherRelated: List<String?>?,
                    @SerialName("__typename")
                    val typename: String?
                )

                @Serializable
                data class Suggestion(
                    @SerialName("currentKeyword")
                    val currentKeyword: String?,
                    @SerialName("suggestion")
                    val suggestion: String?,
                    @SerialName("suggestionCount")
                    val suggestionCount: Int?,
                    @SerialName("instead")
                    val instead: String?,
                    @SerialName("insteadCount")
                    val insteadCount: Int?,
                    @SerialName("query")
                    val query: String?,
                    @SerialName("text")
                    val text: String?,
                    @SerialName("componentId")
                    val componentId: String?,
                    @SerialName("trackingOption")
                    val trackingOption: Int?,
                    @SerialName("__typename")
                    val typename: String?
                )

                @Serializable
                data class Product(
                    @SerialName("id")
                    val id: Long,
                    @SerialName("name")
                    val name: String,
                    @SerialName("ads")
                    val ads: Ads?,
                    @SerialName("badges")
                    val badges: List<Badge?>?,
                    @SerialName("category")
                    val category: Int?,
                    @SerialName("categoryBreadcrumb")
                    val categoryBreadcrumb: String?,
                    @SerialName("categoryId")
                    val categoryId: Int?,
                    @SerialName("categoryName")
                    val categoryName: String?,
                    @SerialName("countReview")
                    val countReview: Int?,
                    @SerialName("customVideoURL")
                    val customVideoURL: String?,
                    @SerialName("discountPercentage")
                    val discountPercentage: Int?,
                    @SerialName("gaKey")
                    val gaKey: String?,
                    @SerialName("imageUrl")
                    val imageUrl: String,
                    @SerialName("labelGroups")
                    val labelGroups: List<LabelGroup?>?,
                    @SerialName("originalPrice")
                    val originalPrice: String,
                    @SerialName("price")
                    val price: String,
                    @SerialName("priceRange")
                    val priceRange: String?,
                    @SerialName("rating")
                    val rating: Int?,
                    @SerialName("ratingAverage")
                    val ratingAverage: String?,
                    @SerialName("shop")
                    val shop: Shop,
                    @SerialName("url")
                    val url: String,
                    @SerialName("wishlist")
                    val wishlist: Boolean?,
                    @SerialName("sourceEngine")
                    val sourceEngine: String?,
                    @SerialName("__typename")
                    val typename: String?
                ) {
                    @Serializable
                    data class Ads(
                        @SerialName("adsId")
                        val adsId: String?,
                        @SerialName("productClickUrl")
                        val productClickUrl: String?,
                        @SerialName("productWishlistUrl")
                        val productWishlistUrl: String?,
                        @SerialName("productViewUrl")
                        val productViewUrl: String?,
                        @SerialName("__typename")
                        val typename: String?
                    )

                    @Serializable
                    data class Badge(
                        @SerialName("title")
                        val title: String?,
                        @SerialName("imageUrl")
                        val imageUrl: String?,
                        @SerialName("show")
                        val show: Boolean?,
                        @SerialName("__typename")
                        val typename: String?
                    )

                    @Serializable
                    data class LabelGroup(
                        @SerialName("position")
                        val position: String?,
                        @SerialName("title")
                        val title: String?,
                        @SerialName("type")
                        val type: String?,
                        @SerialName("url")
                        val url: String?,
                        @SerialName("__typename")
                        val typename: String?
                    )

                    @Serializable
                    data class Shop(
                        @SerialName("shopId")
                        val shopId: Int?,
                        @SerialName("name")
                        val name: String?,
                        @SerialName("url")
                        val url: String?,
                        @SerialName("city")
                        val city: String,
                        @SerialName("isOfficial")
                        val isOfficial: Boolean?,
                        @SerialName("isPowerBadge")
                        val isPowerBadge: Boolean?,
                        @SerialName("__typename")
                        val typename: String?
                    )
                }

                @Serializable
                data class Violation(
                    @SerialName("headerText")
                    val headerText: String?,
                    @SerialName("descriptionText")
                    val descriptionText: String?,
                    @SerialName("imageURL")
                    val imageURL: String?,
                    @SerialName("ctaURL")
                    val ctaURL: String?,
                    @SerialName("ctaApplink")
                    val ctaApplink: String?,
                    @SerialName("buttonText")
                    val buttonText: String?,
                    @SerialName("buttonType")
                    val buttonType: String?,
                    @SerialName("__typename")
                    val typename: String?
                )
            }
        }
    }
}
