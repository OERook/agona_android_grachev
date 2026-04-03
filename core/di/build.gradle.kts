plugins {
    id("reparo.android.library")
    id("reparo.android.dagger")
    id("com.google.devtools.ksp")
}

android {
    namespace = "ru.itis.android.di"
}

dependencies {
    implementation(project(path=":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
}
