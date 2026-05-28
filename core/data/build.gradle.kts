plugins {
    id("reparo.android.library")
    id("reparo.android.dagger")
}

android {
    namespace = "ru.itis.android.reparo.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    // STOMP-over-WebSocket — Krossbow handles framing, heartbeats and reconnect
    // hooks; we bring our own OkHttp engine for consistency with the REST stack.
    implementation("org.hildan.krossbow:krossbow-stomp-core:5.4.0")
    implementation("org.hildan.krossbow:krossbow-websocket-okhttp:5.4.0")
    implementation("com.google.code.gson:gson:2.10.1")
}
