package com.ramusthastudio.ecommerce

import com.ramusthastudio.ecommerce.common.Helper.Companion.bukalapakCommonRequest
import com.ramusthastudio.ecommerce.httpclient.EcommerceClientApi
import com.ramusthastudio.ecommerce.model.EcommerceHost
import kotlinx.coroutines.delay

suspend fun main() {
    var clientApi = EcommerceClientApi()
    clientApi.searchProduct(EcommerceHost.BUKALAPAK, bukalapakCommonRequest("batocera"))

    delay(1000)
}

