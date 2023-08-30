package com.ramusthastudio.ecommerce.mapper

import com.ramusthastudio.ecommerce.model.BlibliSearchResponse
import com.ramusthastudio.ecommerce.model.BukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.TokopediaSearchResponse
import java.math.BigDecimal

fun String.toNumeric(): BigDecimal {
    val numericString = this.replace(Regex("\\D"), "")
    if (numericString.isBlank()) return BigDecimal.ZERO
    return BigDecimal(numericString)
}

fun convertBukalapakSearchResponse(
    responseTime: Long,
    bukalapakSearchResponse: BukalapakSearchResponse
): CommonSearchResponse {
    return CommonSearchResponse(
        bukalapakSearchResponse.data.stream()
            .map { convertBukalapakData(it) }
            .toList(),
        convertBukalapakMeta(
            responseTime,
            bukalapakSearchResponse.meta
        )
    )
}

fun convertBukalapakData(responseData: BukalapakSearchResponse.Data): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        responseData.id,
        responseData.name,
        responseData.condition,
        responseData.active,
        responseData.description,
        responseData.price,
        responseData.originalPrice,
        responseData.store.name,
        responseData.store.address.city,
        responseData.url,
        responseData.images.largeUrls.first(),
    )
}

fun convertBukalapakMeta(
    responseTime: Long,
    responseMeta: BukalapakSearchResponse.Meta
): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        responseMeta.page,
        responseMeta.perPage ?: -1,
        responseMeta.total ?: -1,
        responseMeta.totalPages ?: -1,
        EcommerceSource.BUKALAPAK.toString(),
        responseTime,
    )
}

fun convertBlibliSearchResponse(
    responseTime: Long,
    blibliSearchResponse: BlibliSearchResponse
): CommonSearchResponse {
    return CommonSearchResponse(
        blibliSearchResponse.data.products.stream()
            .map { convertBlibliData(it) }
            .toList(),
        convertBlibliMeta(
            responseTime,
            blibliSearchResponse.data.paging
        )
    )
}

fun convertBlibliData(responseData: BlibliSearchResponse.Data.Product): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        responseData.id,
        responseData.name,
        null,
        true,
        responseData.uniqueSellingPoint,
        responseData.price.offerPriceDisplay.toNumeric(),
        responseData.price.priceDisplay.toNumeric(),
        responseData.merchantName,
        responseData.location,
        "https://" + EcommerceSource.BLIBLI.host + responseData.url,
        responseData.images.first(),
    )
}

fun convertBlibliMeta(
    responseTime: Long,
    responseMeta: BlibliSearchResponse.Data.Paging
): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        responseMeta.page,
        responseMeta.itemPerPage,
        responseMeta.totalItem,
        responseMeta.totalPage,
        EcommerceSource.BLIBLI.toString(),
        responseTime,
    )
}

fun convertTokopediaSearchResponse(
    responseTime: Long,
    tokopediaSearchResponse: TokopediaSearchResponse
): CommonSearchResponse {
    return CommonSearchResponse(
        tokopediaSearchResponse.data.aceSearchProductV4.data.products
            .map { convertTokopediaData(it) }
            .toList(),
        convertTokopediaMeta(
            responseTime,
            tokopediaSearchResponse.data.aceSearchProductV4.header
        )
    )
}

fun convertTokopediaData(
    responseData: TokopediaSearchResponse.Data.AceSearchProductV4.Data.Product
): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        responseData.id.toString(),
        responseData.name,
        null,
        true,
        null,
        responseData.price.toNumeric(),
        responseData.originalPrice.toNumeric(),
        responseData.shop.name,
        responseData.shop.city,
        responseData.url,
        responseData.imageUrl,
    )
}

fun convertTokopediaMeta(
    responseTime: Long,
    responseMeta: TokopediaSearchResponse.Data.AceSearchProductV4.Header
): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        total = responseMeta.totalData,
        source = EcommerceSource.TOKOPEDIA.toString(),
        priority = responseTime,
    )
}
