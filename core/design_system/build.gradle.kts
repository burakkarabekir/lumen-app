plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    android {
        namespace = "com.bksd.core.design_system"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
                implementation(libs.jetbrains.kotlinx.collections.immutable)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.bksd.core.design_system"
}