plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    android {
        namespace = "com.bksd.core.billing"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)

                implementation(libs.koin.core)
                implementation(libs.kermit)
                implementation(libs.kotlinx.coroutines.core)

                api(libs.revenuecat.purchases)
            }
        }
    }
}
