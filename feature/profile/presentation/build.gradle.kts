plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    android {
        namespace = "com.bksd.profile.presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.profile.domain)
                implementation(projects.feature.auth.domain)

                implementation(libs.coil.compose)
                implementation(libs.bundles.datastore)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.profile.presentation"
}
