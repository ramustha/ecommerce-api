package com.ramusthastudio.ecommerce.common

import java.io.File

fun String.asResource(): String {
    return File(ClassLoader.getSystemResource(this).file).readText()
}
