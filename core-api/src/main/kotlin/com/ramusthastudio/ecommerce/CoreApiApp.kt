package com.ramusthastudio.ecommerce

import com.ramusthastudio.ecommerce.common.bukalapakCommonRequest
import com.ramusthastudio.ecommerce.httpclient.EcommerceClientApiImpl
import com.ramusthastudio.ecommerce.model.EcommerceHost
import kotlinx.coroutines.delay

suspend fun main() {
    val clientApi = EcommerceClientApiImpl()
    val searchProduct = clientApi.searchProduct(
        EcommerceHost.BUKALAPAK,
        bukalapakCommonRequest("batocera")
    )
    delay(1000)

    println("response ${searchProduct}")
}

