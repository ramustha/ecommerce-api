plugins {
    kotlin("jvm") apply false
    kotlin("plugin.serialization") version "1.9.0"
}

tasks {
    create("stage").dependsOn("build")
}
