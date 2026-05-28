plugins {
    id("com.android.application") version "8.9.1" apply false
    id("com.android.library") version "8.9.1" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0" apply false
}

// Coil 3.4 (and a few other recent libs) transitively pull kotlin-stdlib 2.3.10,
// whose binary metadata our 2.1.0 compiler can't read. Pin every kotlin-stdlib
// variant to the project's Kotlin version so the classpath stays consistent.
subprojects {
    configurations.configureEach {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin"
                && requested.name.startsWith("kotlin-stdlib")
            ) {
                useVersion("2.1.0")
            }
        }
    }
}
