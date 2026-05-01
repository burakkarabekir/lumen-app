plugins {
    alias(libs.plugins.convention.kmp.data)
}

kotlin {
    android {
        namespace = "com.bksd.moment.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.moment.domain)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
