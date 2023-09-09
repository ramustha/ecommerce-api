package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchRequest
import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceSource
import com.ramusthastudio.ecommerce.model.commonSearchRequest
import io.ktor.utils.io.core.Closeable

interface EcommerceClientApi : Closeable {

    suspend fun searchProductCombine(
        commonSearchRequest: CommonSearchRequest = commonSearchRequest()
    ): List<CommonSearchResponse>

    suspend fun searchProduct(
        ecommerceSource: EcommerceSource,
        commonSearchRequest: CommonSearchRequest = commonSearchRequest(),
        content: String? = null
    ): CommonSearchResponse
}

