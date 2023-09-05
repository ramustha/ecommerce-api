package com.ramusthastudio.ecommerce.httpclient

import com.ramusthastudio.ecommerce.model.CommonSearchResponse
import java.util.regex.Pattern

interface ClientEngine {
    suspend fun searchByRestful(): CommonSearchResponse
    suspend fun searchByScraper(content: String? = null): CommonSearchResponse

    fun replaceScriptTag(html: String): String {
        val replacements = mapOf(
            "</script>\n<script type=\"application/ld+json\" data-rh=\"true\">" to ",",
            "<script type=\"application/ld+json\" data-rh=\"true\">" to "[",
            "</script>" to "]"
        )

        val sb = StringBuilder(html)
        for ((oldValue, newValue) in replacements) {
            var index = sb.indexOf(oldValue)
            while (index != -1) {
                sb.replace(index, index + oldValue.length, newValue)
                index += newValue.length
                index = sb.indexOf(oldValue, index)
            }
        }
        return sb.toString()
    }

    fun removeScriptTag(str: String): String {
        val pattern = Pattern.compile("<script[^>]*>(.*?)</script>")
        val matcher = pattern.matcher(str)
        return if (matcher.find()) {
            matcher.group(1)
        } else str
    }
}
