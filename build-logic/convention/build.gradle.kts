plugins {
    `kotlin-dsl`
}

group = "ru.itis.android.reparo.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "reparo.android.application"
            implementationClass = "ru.itis.android.reparo.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "reparo.android.library"
            implementationClass = "ru.itis.android.reparo.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "reparo.android.compose"
            implementationClass = "ru.itis.android.reparo.AndroidComposeConventionPlugin"
        }
        register("androidRoom") {
            id = "reparo.android.room"
            implementationClass = "ru.itis.android.reparo.AndroidRoomConventionPlugin"
        }
        register("androidDagger") {
            id = "reparo.android.dagger"
            implementationClass = "ru.itis.android.reparo.AndroidDaggerConventionPlugin"
        }
    }
}
