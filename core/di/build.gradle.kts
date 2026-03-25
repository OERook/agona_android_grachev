plugins {
    id("reparo.android.library")
    id("reparo.android.dagger")
}

android {
    namespace = "ru.itis.android.di"
}

dependencies {
    implementation(project(path=":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
}
