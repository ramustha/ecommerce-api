package com.ramusthastudio.ecommerce.mapper

import com.ramusthastudio.ecommerce.model.BlibliSearchResponse
import com.ramusthastudio.ecommerce.model.BukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceHost
import java.math.BigDecimal

fun convertNonNumeric(input: String): BigDecimal = BigDecimal(input.replace(Regex("[^\\d]"), ""))

fun convertBukalapakSearchResponse(bukalapakSearchResponse: BukalapakSearchResponse): CommonSearchResponse {
    return CommonSearchResponse(
        bukalapakSearchResponse.data.stream()
            .map { convertBukalapakData(it) }
            .toList(),
        convertBukalapakMeta(bukalapakSearchResponse.meta)
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
        responseData.store.address.city,
        responseData.store.address.province,
        responseData.url,
        responseData.videoUrl,
        CommonSearchResponse.Data.Images(
            responseData.images.largeUrls,
            responseData.images.smallUrls,
        ),
    )
}

fun convertBukalapakMeta(responseMeta: BukalapakSearchResponse.Meta): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        responseMeta.page,
        responseMeta.perPage,
        responseMeta.total,
        responseMeta.totalPages,
    )
}

fun convertBlibliSearchResponse(blibliSearchResponse: BlibliSearchResponse): CommonSearchResponse {
    return CommonSearchResponse(
        blibliSearchResponse.data.products.stream()
            .map { convertBlibliData(it) }
            .toList(),
        convertBlibliMeta(blibliSearchResponse.data.paging)
    )
}

fun convertBlibliData(responseData: BlibliSearchResponse.Data.Product): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        responseData.id,
        responseData.name,
        null,
        true,
        responseData.uniqueSellingPoint,
        convertNonNumeric(responseData.price.offerPriceDisplay),
        convertNonNumeric(responseData.price.priceDisplay),
        responseData.location,
        null,
        "https://" + EcommerceHost.BLIBLI.host + responseData.url,
        null,
        CommonSearchResponse.Data.Images(
            emptyList(),
            responseData.images,
        ),
    )
}

fun convertBlibliMeta(responseMeta: BlibliSearchResponse.Data.Paging): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        responseMeta.page,
        responseMeta.itemPerPage,
        responseMeta.totalItem,
        responseMeta.totalPage,
    )
}
