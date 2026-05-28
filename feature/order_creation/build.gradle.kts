plugins {
    id("reparo.android.library")
    id("reparo.android.compose")
    id("reparo.android.dagger")
}

android {
    namespace = "ru.itis.android.order_creation"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:presentation"))
    implementation(project(":core:di"))
}
