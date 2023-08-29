package com.ramusthastudio.ecommerce

import com.ramusthastudio.ecommerce.httpclient.EcommerceClientApiImpl
import com.ramusthastudio.ecommerce.model.EcommerceHost
import kotlinx.coroutines.delay

suspend fun main() {
    val clientApi = EcommerceClientApiImpl()
    var searchProduct = clientApi.searchProduct(
        EcommerceHost.BUKALAPAK,
        "batocera"
    )
    delay(1000)

    println("response ${searchProduct}")

    searchProduct = clientApi.searchProduct(
        EcommerceHost.BLIBLI,
        "batocera"
    )
    delay(1000)

    println("response ${searchProduct}")

    searchProduct = clientApi.searchProduct(
        EcommerceHost.TOKOPEDIA,
        "batocera"
    )
    delay(1000)

    println("response ${searchProduct}")
}

