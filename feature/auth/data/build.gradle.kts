plugins {
    alias(libs.plugins.convention.kmp.data)
}

kotlin {
    android {
        namespace = "com.bksd.auth.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.auth.domain)
                implementation(projects.feature.profile.domain)
            }
        }
    }
}
