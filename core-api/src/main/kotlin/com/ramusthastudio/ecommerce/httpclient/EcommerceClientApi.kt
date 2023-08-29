package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import com.ramusthastudio.ecommerce.model.EcommerceHost
import io.ktor.utils.io.core.Closeable

interface EcommerceClientApi : Closeable {

    suspend fun searchProduct(ecommerceHost: EcommerceHost, query: String = ""): CommonSearchResponse
}
