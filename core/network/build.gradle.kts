plugins {
    id("reparo.android.library")
    id("reparo.android.dagger")
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "ru.itis.android.reparo.core.network"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    api(libs.retrofit)
    api(libs.retrofit.gson)
    api(libs.okhttp)
    api(libs.okhttp.logging)
}
