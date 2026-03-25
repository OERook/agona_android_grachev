plugins {
    id("reparo.android.library")
    id("reparo.android.dagger")
}

android {
    namespace = "ru.itis.android.reparo.core.network"
}

dependencies {
    api(libs.retrofit)
    api(libs.retrofit.gson)
    api(libs.okhttp)
    api(libs.okhttp.logging)
}
