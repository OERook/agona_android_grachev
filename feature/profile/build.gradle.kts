plugins {
    id("reparo.android.library")
    id("reparo.android.compose")
    id("reparo.android.dagger")
}

android {
    namespace = "ru.itis.android.profile"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:presentation"))
    implementation(project(":core:di"))

    // Show review attachments inline on the master profile.
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
