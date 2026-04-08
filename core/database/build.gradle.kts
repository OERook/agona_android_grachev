plugins {
    id("reparo.android.library")
    id("reparo.android.room")
    id("reparo.android.dagger")
}

android {
    namespace = "ru.itis.android.reparo.core.database"
}

dependencies {
    implementation(project(":core:domain"))
}
