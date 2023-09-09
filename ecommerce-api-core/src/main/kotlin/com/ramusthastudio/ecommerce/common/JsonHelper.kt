package com.ramusthastudio.ecommerce.common

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal

val CURRENCY_SYMBOLS = listOf("Rp", "Rp.")

fun JsonElement.getJsonValue(key: String) = this.jsonObject[key]?.jsonPrimitive?.content.toString()
fun JsonElement.getJsonObject(key: String) = this.jsonObject[key]?.jsonObject

fun String.removeCurrencySymbols(): String {
    var result = this
    for (symbol in CURRENCY_SYMBOLS) {
        result = result.replace(symbol, "")
    }
    return result
}

fun String.removeDecimalPlaces(): String {
    return if (this.endsWith(".00")) this.substring(0, this.length - 3) else this
}

fun String.currencyFormat(): BigDecimal {
    val numericString = this.removeCurrencySymbols()
        .removeDecimalPlaces()
        .replace(Regex("\\D"), "")
    if (numericString.isBlank()) return BigDecimal("-1")
    return BigDecimal(numericString)
}
