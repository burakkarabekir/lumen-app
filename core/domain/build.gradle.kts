plugins {
    alias(libs.plugins.convention.kmp.domain)
}

kotlin {
    android {
        namespace = "com.bksd.core.domain"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}