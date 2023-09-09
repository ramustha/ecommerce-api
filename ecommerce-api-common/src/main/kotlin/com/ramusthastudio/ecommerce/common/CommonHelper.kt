package com.ramusthastudio.ecommerce.common

import java.io.File

fun String.asResource(): String = File(ClassLoader.getSystemResource(this).file).readText()
fun String.getResourceValue(key: String, default: String): String = asResourceMap().getOrDefault(key, default)
fun String.asResourceMap(): Map<String, String> =
    File(ClassLoader.getSystemResource(this).file)
        .useLines { lines ->
            lines
                .mapNotNull { line ->
                    line
                        .split("|").takeIf { it.size == 2 }?.let { it[0] to it[1] }
                }.toMap()
        }

