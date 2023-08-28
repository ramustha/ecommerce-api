package com.ramusthastudio.ecommerce.model

import com.ramusthastudio.ecommerce.httpclient.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.math.BigDecimal

@Serializable
data class BukalapakSearchResponse(
    @SerialName("data")
    val data: List<Data>,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class Data(
        @SerialName("active")
        val active: Boolean,
        @SerialName("assurance")
        val assurance: Boolean?,
        @SerialName("available_countries")
        val availableCountries: List<JsonObject?>?,
        @SerialName("category")
        val category: Category?,
        @SerialName("condition")
        val condition: String?,
        @SerialName("couriers")
        val couriers: List<String?>?,
        @SerialName("created_at")
        val createdAt: String?,
        @SerialName("deal")
        val deal: Deal?,
        @SerialName("default_catalog")
        val defaultCatalog: JsonObject?,
        @SerialName("description")
        val description: String,
        @SerialName("digital_product")
        val digitalProduct: Boolean?,
        @SerialName("discount_percentage")
        val discountPercentage: Int?,
        @SerialName("discount_subsidy")
        val discountSubsidy: String?,
        @SerialName("dynamic_tags")
        val dynamicTags: List<JsonObject?>?,
        @SerialName("for_sale")
        val forSale: Boolean?,
        @SerialName("id")
        val id: String,
        @SerialName("images")
        val images: Images,
        @SerialName("imported")
        val imported: Boolean?,
        @SerialName("installments")
        val installments: List<JsonObject?>?,
        @SerialName("international_couriers")
        val internationalCouriers: List<String?>?,
        @SerialName("max_quantity")
        val maxQuantity: Int?,
        @SerialName("merchant_return_insurance")
        val merchantReturnInsurance: Boolean?,
        @SerialName("min_quantity")
        val minQuantity: Int?,
        @SerialName("name")
        val name: String,
        @SerialName("original_price")
        @Serializable(with = BigDecimalSerializer::class)
        val originalPrice: BigDecimal,
        @SerialName("price")
        @Serializable(with = BigDecimalSerializer::class)
        val price: BigDecimal,
        @SerialName("product_sin")
        val productSin: List<JsonObject?>?,
        @SerialName("rating")
        val rating: Rating?,
        @SerialName("relisted_at")
        val relistedAt: String?,
        @SerialName("rush_delivery")
        val rushDelivery: Boolean?,
        @SerialName("shipping")
        val shipping: Shipping?,
        @SerialName("sku_id")
        val skuId: Long?,
        @SerialName("sla")
        val sla: Sla?,
        @SerialName("special_campaign_id")
        val specialCampaignId: Int?,
        @SerialName("specs")
        val specs: Specs?,
        @SerialName("state")
        val state: String?,
        @SerialName("state_description")
        val stateDescription: List<JsonObject?>?,
        @SerialName("stats")
        val stats: Stats?,
        @SerialName("stock")
        val stock: Int?,
        @SerialName("store")
        val store: Store,
        @SerialName("tag_pages")
        val tagPages: List<TagPage?>?,
        @SerialName("updated_at")
        val updatedAt: String?,
        @SerialName("url")
        val url: String,
        @SerialName("video_url")
        val videoUrl: String?,
        @SerialName("warranty")
        val warranty: Warranty?,
        @SerialName("weight")
        val weight: Int?,
        @SerialName("wholesales")
        val wholesales: List<JsonObject?>?,
        @SerialName("without_shipping")
        val withoutShipping: Boolean?
    ) {
        @Serializable
        data class Category(
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?,
            @SerialName("structure")
            val structure: List<String?>?,
            @SerialName("url")
            val url: String?
        )

        @Serializable
        data class Deal(
            @SerialName("applied_date")
            val appliedDate: String?,
            @SerialName("discount_price")
            @Serializable(with = BigDecimalSerializer::class)
            val discountPrice: BigDecimal?,
            @SerialName("expired_date")
            val expiredDate: String?,
            @Serializable(with = BigDecimalSerializer::class)
            @SerialName("original_price")
            val originalPrice: BigDecimal?,
            @SerialName("percentage")
            val percentage: Int?
        )

        @Serializable
        data class Images(
            @SerialName("large_urls")
            val largeUrls: List<String>,
            @SerialName("small_urls")
            val smallUrls: List<String>
        )

        @Serializable
        data class Rating(
            @SerialName("average_rate")
            val averageRate: Double?,
            @SerialName("user_count")
            val userCount: Int?
        )

        @Serializable
        data class Shipping(
            @SerialName("force_insurance")
            val forceInsurance: Boolean?,
            @SerialName("free_shipping_coverage")
            val freeShippingCoverage: List<JsonObject?>?
        )

        @Serializable
        data class Sla(
            @SerialName("type")
            val type: String?,
            @SerialName("value")
            val value: Int?
        )

        @Serializable
        data class Specs(
            @SerialName("brand")
            val brand: String?,
            @SerialName("platform")
            val platform: String?
        )

        @Serializable
        data class Stats(
            @SerialName("interest_count")
            val interestCount: Int?,
            @SerialName("sold_count")
            val soldCount: Int?,
            @SerialName("view_count")
            val viewCount: Int?,
            @SerialName("waiting_payment_count")
            val waitingPaymentCount: Int?
        )

        @Serializable
        data class Store(
            @SerialName("address")
            val address: Address,
            @SerialName("alert")
            val alert: String?,
            @SerialName("avatar_url")
            val avatarUrl: String?,
            @SerialName("brand_seller")
            val brandSeller: Boolean?,
            @SerialName("carriers")
            val carriers: List<String?>?,
            @SerialName("closing")
            val closing: Closing?,
            @SerialName("delivery_time")
            val deliveryTime: String?,
            @SerialName("description")
            val description: String?,
            @SerialName("first_upload_product_at")
            val firstUploadProductAt: String?,
            @SerialName("flagship")
            val flagship: Boolean?,
            @SerialName("groups")
            val groups: List<JsonObject?>?,
            @SerialName("header_image")
            val headerImage: HeaderImage?,
            @SerialName("id")
            val id: Int?,
            @SerialName("inactivity")
            val inactivity: Inactivity?,
            @SerialName("last_order_schedule")
            val lastOrderSchedule: LastOrderSchedule?,
            @SerialName("level")
            val level: Level?,
            @SerialName("name")
            val name: String?,
            @SerialName("premium_level")
            val premiumLevel: String?,
            @SerialName("premium_top_seller")
            val premiumTopSeller: Boolean?,
            @SerialName("rejection")
            val rejection: Rejection?,
            @SerialName("reviews")
            val reviews: Reviews?,
            @SerialName("sla")
            val sla: Sla?,
            @SerialName("subscriber_amount")
            val subscriberAmount: Int?,
            @SerialName("term_and_condition")
            val termAndCondition: String?,
            @SerialName("url")
            val url: String?
        ) {
            @Serializable
            data class Address(
                @SerialName("city")
                val city: String,
                @SerialName("province")
                val province: String
            )

            @Serializable
            data class Closing(
                @SerialName("closed")
                val closed: Boolean?
            )

            @Serializable
            data class HeaderImage(
                @SerialName("url")
                val url: String?
            )

            @Serializable
            data class Inactivity(
                @SerialName("inactive")
                val inactive: Boolean?,
                @SerialName("last_appear_at")
                val lastAppearAt: String?
            )

            @Serializable
            data class LastOrderSchedule(
                @SerialName("friday")
                val friday: String?,
                @SerialName("monday")
                val monday: String?,
                @SerialName("saturday")
                val saturday: String?,
                @SerialName("thursday")
                val thursday: String?,
                @SerialName("tuesday")
                val tuesday: String?,
                @SerialName("wednesday")
                val wednesday: String?,
                @SerialName("sunday")
                val sunday: String?
            )

            @Serializable
            data class Level(
                @SerialName("image_url")
                val imageUrl: String?,
                @SerialName("name")
                val name: String?
            )

            @Serializable
            data class Rejection(
                @SerialName("recent_transactions")
                val recentTransactions: Int?,
                @SerialName("rejected")
                val rejected: Int?
            )

            @Serializable
            data class Reviews(
                @SerialName("negative")
                val negative: Int?,
                @SerialName("positive")
                val positive: Int?
            )

            @Serializable
            data class Sla(
                @SerialName("type")
                val type: String?,
                @SerialName("value")
                val value: Int?
            )
        }

        @Serializable
        data class TagPage(
            @SerialName("name")
            val name: String?,
            @SerialName("url")
            val url: String?
        )

        @Serializable
        data class Warranty(
            @SerialName("cheapest")
            val cheapest: Boolean?
        )
    }

    @Serializable
    data class Meta(
        @SerialName("blocked_keywords")
        val blockedKeywords: BlockedKeywords?,
        @SerialName("facets")
        val facets: List<Facet?>?,
        @SerialName("http_status")
        val httpStatus: Int?,
        @SerialName("page")
        val page: Int,
        @SerialName("per_page")
        val perPage: Int,
        @SerialName("position")
        val position: List<Position?>?,
        @SerialName("price_range")
        val priceRange: PriceRange?,
        @SerialName("search_context")
        val searchContext: SearchContext?,
        @SerialName("suggestions")
        val suggestions: Suggestions?,
        @SerialName("total")
        val total: Int,
        @SerialName("total_pages")
        val totalPages: Int
    ) {
        @Serializable
        data class BlockedKeywords(
            @SerialName("blocked")
            val blocked: Boolean?,
            @SerialName("keyword")
            val keyword: String?,
            @SerialName("message")
            val message: String?,
            @SerialName("type")
            val type: String?
        )

        @Serializable
        data class Facet(
            @SerialName("children")
            val children: List<Children?>?,
            @SerialName("count")
            val count: Int?,
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?,
            @SerialName("permalink")
            val permalink: String?
        ) {
            @Serializable
            data class Children(
                @SerialName("children")
                val children: List<Children?>?,
                @SerialName("count")
                val count: Int?,
                @SerialName("id")
                val id: Int?,
                @SerialName("name")
                val name: String?,
                @SerialName("permalink")
                val permalink: String?
            ) {
                @Serializable
                data class Children(
                    @SerialName("children")
                    val children: List<JsonObject?>?,
                    @SerialName("count")
                    val count: Int?,
                    @SerialName("id")
                    val id: Int?,
                    @SerialName("name")
                    val name: String?,
                    @SerialName("permalink")
                    val permalink: String?
                )
            }
        }

        @Serializable
        data class Position(
            @SerialName("id")
            val id: String?,
            @SerialName("type")
            val type: String?
        )

        @Serializable
        data class PriceRange(
            @SerialName("max")
            @Serializable(with = BigDecimalSerializer::class)
            val max: BigDecimal?,
            @SerialName("min")
            @Serializable(with = BigDecimalSerializer::class)
            val min: BigDecimal?
        )

        @Serializable
        data class SearchContext(
            @SerialName("is_keyword_typo")
            val isKeywordTypo: Boolean?,
            @SerialName("search_result_size")
            val searchResultSize: String?
        )

        @Serializable
        data class Suggestions(
            @SerialName("typed_keyword")
            val typedKeyword: String?,
            @SerialName("types")
            val types: List<JsonObject?>?
        )
    }
}
