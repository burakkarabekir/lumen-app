plugins {
    alias(libs.plugins.convention.kmp.data)
    alias(libs.plugins.convention.room)
}

kotlin {
    android {
        namespace = "com.bksd.journal.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.journal.domain)
                implementation(libs.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.collections.immutable)
                implementation(libs.firebase.firestore)
                implementation(libs.kermit)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(project.dependencies.platform(libs.firebase.bom))
            }
        }
    }
}