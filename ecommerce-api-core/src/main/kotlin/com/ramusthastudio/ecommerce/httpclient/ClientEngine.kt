package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse

interface ClientEngine {
    suspend fun searchByRestful(): CommonSearchResponse
    suspend fun searchByScraper(): CommonSearchResponse
}
