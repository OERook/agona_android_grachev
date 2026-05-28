plugins {
    id("reparo.android.application")
    id("reparo.android.compose")
    id("reparo.android.dagger")
    id("reparo.android.room")
    id("reparo.android.navigation")
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
    implementation(project(path=":core:network"))
    implementation(project(path=":core:database"))
    implementation(project(path=":core:presentation"))
    implementation(project(path=":core:navigation"))
    implementation(project(path=":feature:auth"))
    implementation(project(path=":feature:main"))
    implementation(project(path=":feature:search"))
    implementation(project(path=":feature:chat"))
    implementation(project(path=":feature:order_creation"))
    implementation(project(path=":feature:profile"))
    implementation(project(path=":feature:orders"))
    implementation(project(path=":core:di"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
