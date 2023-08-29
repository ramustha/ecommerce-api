package com.ramusthastudio.ecommerce

import java.io.File

fun String.asResource(): String {
    return File(ClassLoader.getSystemResource(this).file).readText()
}
