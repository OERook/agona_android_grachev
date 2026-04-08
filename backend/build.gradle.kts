plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application // Добавляем стандартный плагин application от Gradle вместо ktor.plugin
}

application {
    mainClass.set("ru.itis.android.backend.ApplicationKt")

    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}

dependencies {
    val ktorVersion = "2.3.8"

    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.netty.jvm)

    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)

    implementation(libs.logback.classic)
}