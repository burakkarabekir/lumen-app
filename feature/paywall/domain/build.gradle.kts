plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.paywall.domain"
        compileSdk = 36
        minSdk = 26
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.domain)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.bundles.koin.core.bundle)
            }
        }
    }
}
