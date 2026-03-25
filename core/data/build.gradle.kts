plugins {
    id("reparo.android.library")
}

android {
    namespace = "ru.itis.android.reparo.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
}
