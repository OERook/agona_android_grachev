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
    implementation(project(":core:database"))
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.1.0"))
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation(libs.retrofit)
}
