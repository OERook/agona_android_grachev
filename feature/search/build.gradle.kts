plugins {
    id("reparo.android.library")
    id("reparo.android.compose")
    id("reparo.android.navigation")
}

android {
    namespace = "ru.itis.android.search"
}

dependencies {
    implementation(project(":core:domain"))
}
