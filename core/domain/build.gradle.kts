plugins {
    id("reparo.android.library")

}

android {
    namespace = "ru.itis.android.reparo.core.domain"
}

dependencies {
    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        implementation("javax.inject:javax.inject:1")
    }
}
