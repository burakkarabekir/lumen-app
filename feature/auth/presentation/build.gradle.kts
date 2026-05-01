plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    android {
        namespace = "com.bksd.auth.presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.feature.auth.domain)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.auth.presentation"
}
