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
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    implementation("javax.inject:javax.inject:1")
}
