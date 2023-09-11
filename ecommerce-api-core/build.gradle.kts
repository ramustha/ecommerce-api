/*
 * This file was generated by the Gradle 'init' task.
 */

val ktorVersion: String by project
val playwrightVersion: String by project
val skrapeItHtmlParserVersion: String by project

plugins {
    id("com.ramusthastudio.ecommerce.java-conventions")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

description = "ecommerce-api-core"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":ecommerce-api-common"))

    api("io.ktor:ktor-client-cio:$ktorVersion")
    api("io.ktor:ktor-client-core:$ktorVersion")
    api("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    api("io.ktor:ktor-client-logging:$ktorVersion")
    api("com.microsoft.playwright:playwright:$playwrightVersion")
    api("it.skrape:skrapeit-html-parser:$skrapeItHtmlParserVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}