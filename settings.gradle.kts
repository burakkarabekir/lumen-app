rootProject.name = "Lumen"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":androidApp")
include(":core:presentation")
include(":core:domain")
include(":core:design_system")
include(":core:data")

include(":feature:journal:data")
include(":feature:journal:domain")
include(":feature:journal:presentation")
include(":feature:moment:data")
include(":feature:moment:domain")
include(":feature:moment:presentation")

include(":feature:onboarding:presentation")

include(":feature:auth:data")
include(":feature:auth:domain")
include(":feature:auth:presentation")

include(":feature:insights:presentation")

include(":feature:profile:presentation")

include(":feature:paywall:presentation")