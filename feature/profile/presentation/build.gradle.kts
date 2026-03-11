plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.profile.presentation"
        compileSdk = 36
        minSdk = 26
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.profile.domain)
                implementation(projects.core.presentation)
                implementation(projects.core.designSystem)

                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.lifecycle)
                implementation(libs.bundles.koin.core.bundle)
                implementation(libs.bundles.koin.compose)
                implementation(libs.jetbrains.kotlinx.collections.immutable)

                implementation(libs.coil.compose)
                implementation(libs.bundles.datastore)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.profile.presentation"
}
