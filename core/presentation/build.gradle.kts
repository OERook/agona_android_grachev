plugins {
    id("reparo.android.library")
    id("reparo.android.compose")
}

android {
    namespace = "ru.itis.android.reparo.core.presentation"
}

dependencies {
    implementation(project(":core:domain"))
}
