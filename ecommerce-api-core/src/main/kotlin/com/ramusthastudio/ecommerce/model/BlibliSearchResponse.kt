package com.ramusthastudio.ecommerce.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlibliSearchResponse(
    @SerialName("code")
    val code: Int,
    @SerialName("status")
    val status: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("pageType")
        val pageType: List<String?>?,
        @SerialName("searchTerm")
        val searchTerm: String?,
        @SerialName("suggestions")
        val suggestions: List<String?>?,
        @SerialName("correctedSearchResponses")
        val correctedSearchResponses: List<String?>?,
        @SerialName("ageRestricted")
        val ageRestricted: Boolean?,
        @SerialName("products")
        val products: List<Product>,
        @SerialName("sorting")
        val sorting: Sorting?,
        @SerialName("filters")
        val filters: List<Filter?>?,
        @SerialName("brandFilterCount")
        val brandFilterCount: BrandFilterCount?,
        @SerialName("quickFilters")
        val quickFilters: List<QuickFilter?>?,
        @SerialName("paging")
        val paging: Paging,
        @SerialName("maxOffers")
        val maxOffers: Int?,
        @SerialName("serverCurrentTime")
        val serverCurrentTime: Long?,
        @SerialName("productInfo")
        val productInfo: ProductInfo?,
        @SerialName("code")
        val code: String?,
        @SerialName("selectedCategoryIds")
        val selectedCategoryIds: List<String?>?,
        @SerialName("visibleCategoryIds")
        val visibleCategoryIds: List<String?>?,
        @SerialName("exclusiveCampResponse")
        val exclusiveCampResponse: ExclusiveCampResponse?,
        @SerialName("sellerNewProducts")
        val sellerNewProducts: List<String?>?,
        @SerialName("sellerNewSectionResponse")
        val sellerNewSectionResponse: SellerNewSectionResponse?,
        @SerialName("similarProductsMerchantSearch")
        val similarProductsMerchantSearch: SimilarProductsMerchantSearch?,
        @SerialName("responseFlags")
        val responseFlags: List<String?>?,
        @SerialName("showRestrictedMsg")
        val showRestrictedMsg: Boolean?,
        @SerialName("redirectionUrl")
        val redirectionUrl: String?,
        @SerialName("trackerFields")
        val trackerFields: TrackerFields?,
        @SerialName("productsPerRow")
        val productsPerRow: Int?,
        @SerialName("needMoreSearchResponse")
        val needMoreSearchResponse: Boolean?,
        @SerialName("pageMetadata")
        val pageMetadata: PageMetadata?,
        @SerialName("pageMetaDataResponse")
        val pageMetaDataResponse: PageMetaDataResponse?,
        @SerialName("selectedFilters")
        val selectedFilters: List<String?>?,
        @SerialName("intentAttributes")
        val intentAttributes: IntentAttributes?,
        @SerialName("personalizedAttributes")
        val personalizedAttributes: PersonalizedAttributes?,
        @SerialName("nerAttributes")
        val nerAttributes: NerAttributes?,
        @SerialName("intentApplied")
        val intentApplied: Boolean?,
        @SerialName("nerApplied")
        val nerApplied: Boolean?,
        @SerialName("showAllCategories")
        val showAllCategories: Boolean?,
        @SerialName("searchPageUrl")
        val searchPageUrl: String?,
        @SerialName("catIntentFailed")
        val catIntentFailed: Boolean?,
        @SerialName("sellerAdsPosition")
        val sellerAdsPosition: List<Int?>?,
        @SerialName("sellerAdsPositionWebListView")
        val sellerAdsPositionWebListView: List<Int?>?,
        @SerialName("sponsorProducts")
        val sponsorProducts: List<String?>?,
        @SerialName("relatedSearchTermsPosition")
        val relatedSearchTermsPosition: Int?,
        @SerialName("nearbyLocationFailed")
        val nearbyLocationFailed: Boolean?,
        @SerialName("correctedNearbyLocation")
        val correctedNearbyLocation: String?,
        @SerialName("interspersedCardFilters")
        val interspersedCardFilters: List<String?>?,
        @SerialName("materiallResponse")
        val materiallResponse: Boolean?,
        @SerialName("interspersedCardPos")
        val interspersedCardPos: InterspersedCardPos?,
        @SerialName("topRatedProduct")
        val topRatedProduct: TopRatedProduct?,
        @SerialName("correctedContextualSearchTerm")
        val correctedContextualSearchTerm: String?,
        @SerialName("sponsorProductOS")
        val sponsorProductOS: Boolean?
    ) {
        @Serializable
        data class Product(
            @SerialName("id")
            val id: String,
            @SerialName("sku")
            val sku: String?,
            @SerialName("merchantCode")
            val merchantCode: String?,
            @SerialName("status")
            val status: String?,
            @SerialName("name")
            val name: String,
            @SerialName("price")
            val price: Price,
            @SerialName("images")
            val images: List<String>,
            @SerialName("rootCategory")
            val rootCategory: RootCategory?,
            @SerialName("brand")
            val brand: String?,
            @SerialName("review")
            val review: Review?,
            @SerialName("itemCount")
            val itemCount: Int?,
            @SerialName("defaultSku")
            val defaultSku: String?,
            @SerialName("itemSku")
            val itemSku: String?,
            @SerialName("tags")
            val tags: List<String?>?,
            @SerialName("formattedId")
            val formattedId: String?,
            @SerialName("url")
            val url: String,
            @SerialName("attributes")
            val attributes: List<Attribute?>?,
            @SerialName("productFeatures")
            val productFeatures: String?,
            @SerialName("storeClosingInfo")
            val storeClosingInfo: StoreClosingInfo?,
            @SerialName("promoEndTime")
            val promoEndTime: Long?,
            @SerialName("debugData")
            val debugData: DebugData?,
            @SerialName("allCategories")
            val allCategories: List<String?>?,
            @SerialName("merchantVoucherCount")
            val merchantVoucherCount: Int?,
            @SerialName("expandedProducts")
            val expandedProducts: List<String?>?,
            @SerialName("location")
            val location: String,
            @SerialName("numLocations")
            val numLocations: Int?,
            @SerialName("badge")
            val badge: Badge?,
            @SerialName("size")
            val size: List<String?>?,
            @SerialName("level0Id")
            val level0Id: String?,
            @SerialName("uniqueSellingPoint")
            val uniqueSellingPoint: String,
            @SerialName("isCheap")
            val isCheap: Boolean?,
            @SerialName("merchantName")
            val merchantName: String,
            @SerialName("categoryIdHierarchy")
            val categoryIdHierarchy: List<String?>?,
            @SerialName("cartLogisticOptions")
            val cartLogisticOptions: List<String?>?,
            @SerialName("merchantVoucherMessage")
            val merchantVoucherMessage: String?,
            @SerialName("wholesaleMinQuantity")
            val wholesaleMinQuantity: Int?,
            @SerialName("wholesaleDiscountPercentage")
            val wholesaleDiscountPercentage: Double?,
            @SerialName("wholesaleDiscountParam")
            val wholesaleDiscountParam: String?,
            @SerialName("pickupPointCode")
            val pickupPointCode: String?,
            @SerialName("categoryNameHierarchy")
            val categoryNameHierarchy: List<String?>?,
            @SerialName("variant")
            val variant: String?,
            @SerialName("freshnessDaysCount")
            val freshnessDaysCount: Int?,
            @SerialName("hasVariants")
            val hasVariants: Boolean?,
            @SerialName("preorder")
            val preorder: Boolean?,
            @SerialName("official")
            val official: Boolean?,
            @SerialName("soldRangeCount")
            val soldRangeCount: SoldRangeCount?,
            @SerialName("promoBadgeUrl")
            val promoBadgeUrl: String?,
            @SerialName("campaignInfo")
            val campaignInfo: CampaignInfo?
        ) {
            @Serializable
            data class Price(
                @SerialName("priceDisplay")
                val priceDisplay: String,
                @SerialName("discount")
                val discount: Int?,
                @SerialName("minPrice")
                val minPrice: Double?,
                @SerialName("offerPriceDisplay")
                val offerPriceDisplay: String,
                @SerialName("isPriceRange")
                val isPriceRange: Boolean?,
                @SerialName("strikeThroughPriceDisplay")
                val strikeThroughPriceDisplay: String?
            )

            @Serializable
            data class RootCategory(
                @SerialName("id")
                val id: String?,
                @SerialName("name")
                val name: String?
            )

            @Serializable
            data class Review(
                @SerialName("rating")
                val rating: Int?,
                @SerialName("count")
                val count: Int?,
                @SerialName("absoluteRating")
                val absoluteRating: Double?
            )

            @Serializable
            data class Attribute(
                @SerialName("count")
                val count: Int?,
                @SerialName("optionListingType")
                val optionListingType: String?,
                @SerialName("values")
                val values: List<String?>?
            )

            @Serializable
            data class StoreClosingInfo(
                @SerialName("storeClosed")
                val storeClosed: Boolean?,
                @SerialName("endDate")
                val endDate: Long?,
                @SerialName("delayShipping")
                val delayShipping: Boolean?
            )

            @Serializable
            class DebugData

            @Serializable
            data class Badge(
                @SerialName("merchantBadge")
                val merchantBadge: String?,
                @SerialName("merchantBadgeUrl")
                val merchantBadgeUrl: String?
            )

            @Serializable
            data class SoldRangeCount(
                @SerialName("en")
                val en: String?,
                @SerialName("id")
                val id: String?
            )

            @Serializable
            data class CampaignInfo(
                @SerialName("name")
                val name: String?,
                @SerialName("code")
                val code: String?
            )
        }

        @Serializable
        data class Sorting(
            @SerialName("parameter")
            val parameter: String?,
            @SerialName("options")
            val options: List<Option?>?
        ) {
            @Serializable
            data class Option(
                @SerialName("label")
                val label: String?,
                @SerialName("name")
                val name: String?,
                @SerialName("value")
                val value: Int?,
                @SerialName("selected")
                val selected: Boolean?
            )
        }

        @Serializable
        data class Filter(
            @SerialName("groupName")
            val groupName: String?,
            @SerialName("grouped")
            val grouped: Boolean?,
            @SerialName("name")
            val name: String?,
            @SerialName("type")
            val type: String?,
            @SerialName("searchable")
            val searchable: Boolean?,
            @SerialName("parameter")
            val parameter: String?,
            @SerialName("theme")
            val theme: String?,
            @SerialName("data")
            val data: List<Data?>?,
            @SerialName("sublist")
            val sublist: List<Sublist?>?,
            @SerialName("horizontal")
            val horizontal: Boolean?,
            @SerialName("label")
            val label: String?,
            @SerialName("parentFacets")
            val parentFacets: List<String?>?
        ) {
            @Serializable
            data class Data(
                @SerialName("label")
                val label: String?,
                @SerialName("value")
                val value: String?,
                @SerialName("indexName")
                val indexName: String?,
                @SerialName("selected")
                val selected: Boolean?,
                @SerialName("valid")
                val valid: Boolean?,
                @SerialName("popular")
                val popular: Boolean?,
                @SerialName("parameter")
                val parameter: String?,
                @SerialName("query")
                val query: String?,
                @SerialName("id")
                val id: String?,
                @SerialName("level")
                val level: Int?,
                @SerialName("subCategory")
                val subCategory: List<String?>?,
                @SerialName("subCategorySelected")
                val subCategorySelected: Boolean?,
                @SerialName("restrictedCategory")
                val restrictedCategory: Boolean?,
                @SerialName("logoUrl")
                val logoUrl: String?,
                @SerialName("code")
                val code: String?,
                @SerialName("disabled")
                val disabled: Boolean?,
                @SerialName("tooltip")
                val tooltip: String?,
                @SerialName("tooltipUrl")
                val tooltipUrl: String?,
                @SerialName("tooltipText")
                val tooltipText: String?,
                @SerialName("toolTipUrl")
                val toolTipUrl: String?
            )

            @Serializable
            data class Sublist(
                @SerialName("title")
                val title: String?,
                @SerialName("parameter")
                val parameter: String?,
                @SerialName("data")
                val data: List<Data?>?
            ) {
                @Serializable
                data class Data(
                    @SerialName("label")
                    val label: String?,
                    @SerialName("value")
                    val value: String?,
                    @SerialName("indexName")
                    val indexName: String?,
                    @SerialName("selected")
                    val selected: Boolean?,
                    @SerialName("valid")
                    val valid: Boolean?,
                    @SerialName("popular")
                    val popular: Boolean?,
                    @SerialName("logoUrl")
                    val logoUrl: String?
                )
            }
        }

        @Serializable
        data class BrandFilterCount(
            @SerialName("invalid")
            val invalid: Int?,
            @SerialName("valid")
            val valid: Int?
        )

        @Serializable
        data class QuickFilter(
            @SerialName("name")
            val name: String?,
            @SerialName("label")
            val label: String?,
            @SerialName("type")
            val type: String?,
            @SerialName("searchable")
            val searchable: Boolean?,
            @SerialName("parameter")
            val parameter: String?,
            @SerialName("theme")
            val theme: String?,
            @SerialName("data")
            val data: List<Data?>?,
            @SerialName("grouped")
            val grouped: Boolean?,
            @SerialName("horizontal")
            val horizontal: Boolean?,
            @SerialName("groupName")
            val groupName: String?
        ) {
            @Serializable
            data class Data(
                @SerialName("label")
                val label: String?,
                @SerialName("value")
                val value: String?,
                @SerialName("selected")
                val selected: Boolean?,
                @SerialName("valid")
                val valid: Boolean?,
                @SerialName("popular")
                val popular: Boolean?,
                @SerialName("disabled")
                val disabled: Boolean?,
                @SerialName("tooltip")
                val tooltip: String?,
                @SerialName("tooltipUrl")
                val tooltipUrl: String?,
                @SerialName("tooltipText")
                val tooltipText: String?,
                @SerialName("toolTipUrl")
                val toolTipUrl: String?,
                @SerialName("parameter")
                val parameter: String?
            )
        }

        @Serializable
        data class Paging(
            @SerialName("page")
            val page: Int,
            @SerialName("total_page")
            val totalPage: Int,
            @SerialName("item_per_page")
            val itemPerPage: Int,
            @SerialName("total_item")
            val totalItem: Int
        )

        @Serializable
        data class ProductInfo(
            @SerialName("GR9-20429-05182")
            val gR92042905182: GR92042905182?,
            @SerialName("PUC-60025-04842")
            val pUC6002504842: PUC6002504842?,
            @SerialName("HUS-70126-62748")
            val hUS7012662748: HUS7012662748?,
            @SerialName("HIR-70020-00024")
            val hIR7002000024: HIR7002000024?,
            @SerialName("PUC-60025-04841")
            val pUC6002504841: PUC6002504841?,
            @SerialName("MAS-71330-31619")
            val mAS7133031619: MAS7133031619?,
            @SerialName("TOC-29837-00746")
            val tOC2983700746: TOC2983700746?,
            @SerialName("MAS-71330-29132")
            val mAS7133029132: MAS7133029132?,
            @SerialName("TOU-17127-08392")
            val tOU1712708392: TOU1712708392?,
            @SerialName("TUR-70014-56985")
            val tUR7001456985: TUR7001456985?,
            @SerialName("GR9-20429-05183")
            val gR92042905183: GR92042905183?,
            @SerialName("TOU-17127-08391")
            val tOU1712708391: TOU1712708391?
        ) {
            @Serializable
            data class GR92042905182(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class PUC6002504842(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class HUS7012662748(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class HIR7002000024(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class PUC6002504841(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class MAS7133031619(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class TOC2983700746(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class MAS7133029132(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class TOU1712708392(
                @SerialName("campaign_name")
                val campaignName: List<String?>?,
                @SerialName("campaign_code")
                val campaignCode: List<String?>?,
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class TUR7001456985(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class GR92042905183(
                @SerialName("tags")
                val tags: List<String?>?
            )

            @Serializable
            data class TOU1712708391(
                @SerialName("campaign_name")
                val campaignName: List<String?>?,
                @SerialName("campaign_code")
                val campaignCode: List<String?>?,
                @SerialName("tags")
                val tags: List<String?>?
            )
        }

        @Serializable
        data class ExclusiveCampResponse(
            @SerialName("exclusiveCampRow")
            val exclusiveCampRow: Int?,
            @SerialName("promoBgImage")
            val promoBgImage: String?,
            @SerialName("promoTitleImage")
            val promoTitleImage: String?,
            @SerialName("campaignCode")
            val campaignCode: String?
        )

        @Serializable
        data class SellerNewSectionResponse(
            @SerialName("newSellerProdSecRow")
            val newSellerProdSecRow: Int?,
            @SerialName("newSellerProdBadge")
            val newSellerProdBadge: String?,
            @SerialName("newSellerProdTitle")
            val newSellerProdTitle: String?,
            @SerialName("newSellerProdBg")
            val newSellerProdBg: String?
        )

        @Serializable
        data class SimilarProductsMerchantSearch(
            @SerialName("similarProductList")
            val similarProductList: List<String?>?,
            @SerialName("seeAllVisible")
            val seeAllVisible: Boolean?
        )

        @Serializable
        data class TrackerFields(
            @SerialName("is_ner_eligible")
            val isNerEligible: String?,
            @SerialName("group_type")
            val groupType: String?,
            @SerialName("sales_velocity_components")
            val salesVelocityComponents: String?
        )

        @Serializable
        data class PageMetadata(
            @SerialName("productComparisonOverriddenC2s")
            val productComparisonOverriddenC2s: String?,
            @SerialName("productComparisonAllowedC1s")
            val productComparisonAllowedC1s: String?,
            @SerialName("showProductComparison")
            val showProductComparison: String?
        )

        @Serializable
        data class PageMetaDataResponse(
            @SerialName("c2SimilarityMap")
            val c2SimilarityMap: C2SimilarityMap?,
            @SerialName("c3SimilarityMap")
            val c3SimilarityMap: C3SimilarityMap?,
            @SerialName("buyAgainSecRow")
            val buyAgainSecRow: Int?
        ) {
            @Serializable
            data class C2SimilarityMap(
                @SerialName("KA-1000004")
                val kA1000004: List<String?>?,
                @SerialName("KA-1000002")
                val kA1000002: List<String?>?,
                @SerialName("TA-1000003")
                val tA1000003: List<String?>?,
                @SerialName("HA-1000002")
                val hA1000002: List<String?>?,
                @SerialName("TU-1000021")
                val tU1000021: List<String?>?
            )

            @Serializable
            data class C3SimilarityMap(
                @SerialName("UL-1000001")
                val uL1000001: List<String?>?,
                @SerialName("GA-1000002")
                val gA1000002: List<String?>?,
                @SerialName("LA-1000005")
                val lA1000005: List<String?>?,
                @SerialName("LA-1000004")
                val lA1000004: List<String?>?,
                @SerialName("KA-1000009")
                val kA1000009: List<String?>?,
                @SerialName("KA-1000006")
                val kA1000006: List<String?>?,
                @SerialName("SM-1000002")
                val sM1000002: List<String?>?,
                @SerialName("KA-1000007")
                val kA1000007: List<String?>?,
                @SerialName("KA-1000005")
                val kA1000005: List<String?>?,
                @SerialName("DE-1000004")
                val dE1000004: List<String?>?,
                @SerialName("MO-1000003")
                val mO1000003: List<String?>?,
                @SerialName("KA-1000010")
                val kA1000010: List<String?>?,
                @SerialName("JA-1000111")
                val jA1000111: List<String?>?,
                @SerialName("DE-1000010")
                val dE1000010: List<String?>?,
                @SerialName("PO-1000015")
                val pO1000015: List<String?>?,
                @SerialName("JA-1000120")
                val jA1000120: List<String?>?,
                @SerialName("AC-1000001")
                val aC1000001: List<String?>?,
                @SerialName("JA-1000110")
                val jA1000110: List<String?>?,
                @SerialName("JA-1000121")
                val jA1000121: List<String?>?
            )
        }

        @Serializable
        class IntentAttributes

        @Serializable
        class PersonalizedAttributes

        @Serializable
        class NerAttributes

        @Serializable
        data class InterspersedCardPos(
            @SerialName("ROM")
            val rOM: Int?,
            @SerialName("Memori Internal")
            val memoriInternal: Int?,
            @SerialName("Ukuran")
            val ukuran: Int?,
            @SerialName("Optical Zoom")
            val opticalZoom: Int?,
            @SerialName("Kondisi Produk")
            val kondisiProduk: Int?,
            @SerialName("Lokasi toko")
            val lokasiToko: Int?,
            @SerialName("Warna")
            val warna: Int?,
            @SerialName("RAM")
            val rAM: Int?
        )

        @Serializable
        data class TopRatedProduct(
            @SerialName("id")
            val id: String?,
            @SerialName("sku")
            val sku: String?,
            @SerialName("merchantCode")
            val merchantCode: String?,
            @SerialName("status")
            val status: String?,
            @SerialName("name")
            val name: String?,
            @SerialName("price")
            val price: Price?,
            @SerialName("images")
            val images: List<String?>?,
            @SerialName("rootCategory")
            val rootCategory: RootCategory?,
            @SerialName("brand")
            val brand: String?,
            @SerialName("review")
            val review: Review?,
            @SerialName("itemCount")
            val itemCount: Int?,
            @SerialName("defaultSku")
            val defaultSku: String?,
            @SerialName("itemSku")
            val itemSku: String?,
            @SerialName("tags")
            val tags: List<String?>?,
            @SerialName("formattedId")
            val formattedId: String?,
            @SerialName("url")
            val url: String?,
            @SerialName("attributes")
            val attributes: List<Attribute?>?,
            @SerialName("productFeatures")
            val productFeatures: String?,
            @SerialName("storeClosingInfo")
            val storeClosingInfo: StoreClosingInfo?,
            @SerialName("promoEndTime")
            val promoEndTime: Long?,
            @SerialName("debugData")
            val debugData: DebugData?,
            @SerialName("allCategories")
            val allCategories: List<String?>?,
            @SerialName("merchantVoucherCount")
            val merchantVoucherCount: Int?,
            @SerialName("expandedProducts")
            val expandedProducts: List<String?>?,
            @SerialName("location")
            val location: String?,
            @SerialName("numLocations")
            val numLocations: Int?,
            @SerialName("badge")
            val badge: Badge?,
            @SerialName("size")
            val size: List<String?>?,
            @SerialName("level0Id")
            val level0Id: String?,
            @SerialName("uniqueSellingPoint")
            val uniqueSellingPoint: String?,
            @SerialName("isCheap")
            val isCheap: Boolean?,
            @SerialName("merchantName")
            val merchantName: String?,
            @SerialName("soldRangeCount")
            val soldRangeCount: SoldRangeCount?,
            @SerialName("categoryIdHierarchy")
            val categoryIdHierarchy: List<String?>?,
            @SerialName("cartLogisticOptions")
            val cartLogisticOptions: List<String?>?,
            @SerialName("merchantVoucherMessage")
            val merchantVoucherMessage: String?,
            @SerialName("wholesaleMinQuantity")
            val wholesaleMinQuantity: Int?,
            @SerialName("wholesaleDiscountPercentage")
            val wholesaleDiscountPercentage: Double?,
            @SerialName("wholesaleDiscountParam")
            val wholesaleDiscountParam: String?,
            @SerialName("pickupPointCode")
            val pickupPointCode: String?,
            @SerialName("categoryNameHierarchy")
            val categoryNameHierarchy: List<String?>?,
            @SerialName("variant")
            val variant: String?,
            @SerialName("freshnessDaysCount")
            val freshnessDaysCount: Int?,
            @SerialName("hasVariants")
            val hasVariants: Boolean?,
            @SerialName("preorder")
            val preorder: Boolean?,
            @SerialName("official")
            val official: Boolean?
        ) {
            @Serializable
            data class Price(
                @SerialName("priceDisplay")
                val priceDisplay: String?,
                @SerialName("strikeThroughPriceDisplay")
                val strikeThroughPriceDisplay: String?,
                @SerialName("discount")
                val discount: Int?,
                @SerialName("minPrice")
                val minPrice: Double?,
                @SerialName("offerPriceDisplay")
                val offerPriceDisplay: String?,
                @SerialName("isPriceRange")
                val isPriceRange: Boolean?
            )

            @Serializable
            data class RootCategory(
                @SerialName("id")
                val id: String?,
                @SerialName("name")
                val name: String?
            )

            @Serializable
            data class Review(
                @SerialName("rating")
                val rating: Int?,
                @SerialName("count")
                val count: Int?,
                @SerialName("absoluteRating")
                val absoluteRating: Double?
            )

            @Serializable
            data class Attribute(
                @SerialName("count")
                val count: Int?
            )

            @Serializable
            data class StoreClosingInfo(
                @SerialName("storeClosed")
                val storeClosed: Boolean?,
                @SerialName("endDate")
                val endDate: Int?,
                @SerialName("delayShipping")
                val delayShipping: Boolean?
            )

            @Serializable
            class DebugData

            @Serializable
            data class Badge(
                @SerialName("merchantBadge")
                val merchantBadge: String?,
                @SerialName("merchantBadgeUrl")
                val merchantBadgeUrl: String?
            )

            @Serializable
            data class SoldRangeCount(
                @SerialName("en")
                val en: String?,
                @SerialName("id")
                val id: String?
            )
        }
    }
}
