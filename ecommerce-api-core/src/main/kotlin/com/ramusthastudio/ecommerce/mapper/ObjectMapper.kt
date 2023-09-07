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
    processTime: Long,
    bukalapakSearchResponse: BukalapakSearchResponse
): CommonSearchResponse {
    return CommonSearchResponse(
        bukalapakSearchResponse.data.stream()
            .map { convertBukalapakData(it) }
            .toList(),
        convertBukalapakMeta(
            processTime,
            bukalapakSearchResponse.meta
        )
    )
}

fun convertBukalapakData(responseData: BukalapakSearchResponse.Data): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        id = responseData.id,
        name = responseData.name,
        price = responseData.price,
        originalPrice = responseData.originalPrice,
        storeName = responseData.store.name,
        storeAddressCity = responseData.store.address.city,
        url = responseData.url,
        imagesUrl = responseData.images.largeUrls.first(),
    )
}

fun convertBukalapakMeta(
    processTime: Long,
    responseMeta: BukalapakSearchResponse.Meta
): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        responseMeta.page,
        responseMeta.perPage ?: -1,
        responseMeta.total ?: -1,
        responseMeta.totalPages ?: -1,
        EcommerceSource.BUKALAPAK.toString(),
        processTime,
    )
}

fun convertBlibliSearchResponse(
    processTime: Long,
    blibliSearchResponse: BlibliSearchResponse
): CommonSearchResponse {
    return CommonSearchResponse(
        blibliSearchResponse.data.products.stream()
            .map { convertBlibliData(it) }
            .toList(),
        convertBlibliMeta(
            processTime,
            blibliSearchResponse.data.paging
        )
    )
}

fun convertBlibliData(responseData: BlibliSearchResponse.Data.Product): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        id = responseData.id,
        name = responseData.name,
        price = responseData.price.offerPriceDisplay.toNumeric(),
        originalPrice = responseData.price.priceDisplay.toNumeric(),
        storeName = responseData.merchantName,
        storeAddressCity = responseData.location,
        url = "https://" + EcommerceSource.BLIBLI.host + responseData.url,
        imagesUrl = responseData.images.first(),
    )
}

fun convertBlibliMeta(
    processTime: Long,
    responseMeta: BlibliSearchResponse.Data.Paging
): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        responseMeta.page,
        responseMeta.itemPerPage,
        responseMeta.totalItem,
        responseMeta.totalPage,
        EcommerceSource.BLIBLI.toString(),
        processTime,
    )
}

fun convertTokopediaSearchResponse(
    processTime: Long,
    tokopediaSearchResponse: TokopediaSearchResponse
): CommonSearchResponse {
    return CommonSearchResponse(
        tokopediaSearchResponse.data.aceSearchProductV4.data.products
            .map { convertTokopediaData(it) }
            .toList(),
        convertTokopediaMeta(
            processTime,
            tokopediaSearchResponse.data.aceSearchProductV4.header
        )
    )
}

fun convertTokopediaData(
    responseData: TokopediaSearchResponse.Data.AceSearchProductV4.Data.Product
): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        id = responseData.id.toString(),
        name = responseData.name,
        price = responseData.price.toNumeric(),
        originalPrice = responseData.originalPrice.toNumeric(),
        storeName = responseData.shop.name,
        storeAddressCity = responseData.shop.city,
        url = responseData.url,
        imagesUrl = responseData.imageUrl,
    )
}

fun convertTokopediaMeta(
    processTime: Long,
    responseMeta: TokopediaSearchResponse.Data.AceSearchProductV4.Header
): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        total = responseMeta.totalData,
        source = EcommerceSource.TOKOPEDIA.toString(),
        processTime = processTime,
    )
}
