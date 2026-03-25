plugins {
    id("reparo.android.library")
    id("reparo.android.room")
}

android {
    namespace = "ru.itis.android.reparo.core.database"
}

dependencies {
    implementation(project(":core:domain"))
}
