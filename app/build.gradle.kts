plugins {
    id("reparo.android.application")
    id("reparo.android.compose")
    id("reparo.android.dagger")
    id("reparo.android.room")
}

android {
    namespace = "ru.itis.android.reparo"

    defaultConfig {
        applicationId = "ru.itis.android.reparo"
    }
}

dependencies {
    implementation(project(path=":core:domain"))
    implementation(project(path=":core:data"))
    implementation(project(path=":core:presentation"))
    implementation(project(path=":feature:auth"))
    implementation(project(path=":core:di"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
}
