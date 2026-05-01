plugins {
    alias(libs.plugins.convention.kmp.domain)
}

kotlin {
    android {
        namespace = "com.bksd.journal.domain"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}