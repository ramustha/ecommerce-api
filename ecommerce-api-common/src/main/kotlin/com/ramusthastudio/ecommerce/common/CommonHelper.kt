package com.ramusthastudio.ecommerce.common

fun String.asResource(): String = ClassLoader.getSystemResourceAsStream(this)?.reader()?.readText() ?: ""
fun String.getResourceValue(key: String, default: String): String = asResourceMap().getOrDefault(key, default)
fun String.asResourceMap(): Map<String, String> =
    ClassLoader.getSystemResourceAsStream(this)?.reader()
        ?.useLines { lines ->
            lines
                .mapNotNull { line ->
                    line
                        .split("|").takeIf { it.size == 2 }?.let { it[0] to it[1] }
                }.toMap()
        } ?: emptyMap()

