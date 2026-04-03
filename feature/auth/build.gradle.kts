plugins {
    id("reparo.android.library")
    id("reparo.android.compose")
    id("reparo.android.dagger")
    id("reparo.android.navigation")
}

android {
    namespace = "ru.itis.android.reparo.feature.auth"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:presentation"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:di"))
    implementation(libs.retrofit)
}
