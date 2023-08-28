package com.ramusthastudio.ecommerce.mapper

import com.ramusthastudio.ecommerce.model.BukalapakSearchResponse
import com.ramusthastudio.ecommerce.model.CommonSearchResponse

fun convertBukalapakSearchResponse(bukalapakSearchResponse: BukalapakSearchResponse): CommonSearchResponse {
    bukalapakSearchResponse.data.stream()
        .map { convertBukalapakData(it) }
        .toList()
    return CommonSearchResponse(
        bukalapakSearchResponse.data.stream()
            .map { convertBukalapakData(it) }
            .toList(),
        convertBukalapakMeta(bukalapakSearchResponse.meta)
    )
}

fun convertBukalapakData(bukalapakData: BukalapakSearchResponse.Data): CommonSearchResponse.Data {
    return CommonSearchResponse.Data(
        bukalapakData.id,
        bukalapakData.name,
        bukalapakData.condition,
        bukalapakData.active,
        bukalapakData.description,
        bukalapakData.price,
        bukalapakData.originalPrice,
        bukalapakData.store.address.city,
        bukalapakData.store.address.province,
        bukalapakData.url,
        bukalapakData.videoUrl,
        CommonSearchResponse.Data.Images(
            bukalapakData.images?.largeUrls,
            bukalapakData.images?.smallUrls,
        ),
    )
}

fun convertBukalapakMeta(bukalapakData: BukalapakSearchResponse.Meta): CommonSearchResponse.Meta {
    return CommonSearchResponse.Meta(
        bukalapakData.page,
        bukalapakData.perPage,
        bukalapakData.total,
        bukalapakData.totalPages,
    )
}
