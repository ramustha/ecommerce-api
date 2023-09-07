package com.ramusthastudio.ecommerce.common

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal

fun JsonElement.getJsonValue(key: String) = this.jsonObject[key]?.jsonPrimitive?.content.toString()
fun JsonElement.getJsonObject(key: String) = this.jsonObject[key]?.jsonObject
fun String.toBigDecimalOr(value: BigDecimal): BigDecimal = this.toBigDecimalOrNull() ?: value
