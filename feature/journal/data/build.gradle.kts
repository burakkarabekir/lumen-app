plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.room)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.journal.data"
        compileSdk = 36
        minSdk = 26
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.data)
                implementation(projects.core.domain)
                implementation(projects.feature.journal.domain)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.insert.koin.koin.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.collections.immutable)
                implementation(libs.firebase.firestore)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.insert.koin.koin.android)
                implementation(project.dependencies.platform(libs.firebase.bom))
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}