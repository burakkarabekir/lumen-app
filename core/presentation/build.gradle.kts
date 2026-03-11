plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.core.presentation"
        compileSdk = 36
        minSdk = 26
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.domain)
                api(projects.core.designSystem)

                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.datetime)
                implementation(libs.coil.compose)

                implementation(libs.bundles.jetbrains.adaptive)
                implementation(libs.bundles.lifecycle)
                implementation(libs.bundles.koin.core.bundle)

                implementation(libs.moko.permissions)
                implementation(libs.moko.permissions.compose)
                implementation(libs.moko.permissions.microphone)
                implementation(libs.moko.permissions.location)
                implementation(libs.moko.permissions.camera)
                implementation(libs.moko.permissions.gallery)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation("com.google.android.gms:play-services-location:21.3.0")
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.core.presentation"
}