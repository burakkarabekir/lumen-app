plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.auth.data"
        compileSdk = 36
        minSdk = 26
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.data)
                implementation(projects.feature.auth.domain)

                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.bundles.koin.core.bundle)
            }
        }
    }
}
