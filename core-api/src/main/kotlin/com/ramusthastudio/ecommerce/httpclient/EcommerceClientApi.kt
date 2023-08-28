package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceHost

interface EcommerceClientApi {
    suspend fun searchProduct(
        ecommerceHost: EcommerceHost,
        query: String
    ): CommonSearchResponse
}
