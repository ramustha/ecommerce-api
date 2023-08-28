package com.ramusthastudio.ecommerce.common

import com.ramusthastudio.ecommerce.model.CommonWebRequest

class Helper {
    companion object {
        fun blibliCommonRequest(query: String): CommonWebRequest {
            return CommonWebRequest(
                page = "1",
                offset = "0",
                xparam = mutableMapOf("channelId" to "web"),
                query = query
            )
        }

        fun bukalapakCommonRequest(query: String): CommonWebRequest {
            val xparam: Map<String, String> = HashMap()
            xparam.plus(Pair("limit", "50"))

            return CommonWebRequest(
                page = "1",
                offset = "0",
                xparam = mutableMapOf("limit" to "50"),
                query = query
            )
        }
    }
}
