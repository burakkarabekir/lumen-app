plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    android {
        namespace = "com.bksd.moment.domain"
        compileSdk = 37
        minSdk = 26
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.domain)
                implementation(projects.feature.journal.domain)
                implementation(libs.kotlinx.datetime)
                implementation(libs.koin.core)
            }
        }
    }
}
