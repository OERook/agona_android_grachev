pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Reparo"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:database")
include(":core:network")
include(":feature:auth")
include(":core:di")

include(":core:navigation")
include(":feature:main")
include(":feature:search")
include(":feature:chat")
include(":feature:order_creation")
include(":feature:orders")
include(":feature:profile")
