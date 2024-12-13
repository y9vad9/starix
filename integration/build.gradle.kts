import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(libs.plugins.conventions.jvm.get().pluginId)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    // -- KotlinX Libraries --
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines)

    // -- Ktor Client (HTTP) --
    api(libs.ktor.client.core)
    api(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.json)

    // -- Database --
    implementation(libs.exposed.core)

    // -- Cache --
    implementation(libs.cache4k)

    // -- Project --
    implementation(projects.core)
    implementation(projects.foundation.validation)
    implementation(projects.foundation.time)
    implementation(projects.localization)

    // -- Telegram Bot API --
    implementation(libs.tgBotApi)
}