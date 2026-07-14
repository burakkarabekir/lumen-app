plugins {
    alias(libs.plugins.convention.kmp.data)
}

kotlin {
    android {
        namespace = "com.bksd.onboarding.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.onboarding.domain)
                implementation(libs.bundles.datastore)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.koin.android)
            }
        }
    }
}
