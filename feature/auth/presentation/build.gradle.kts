plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.auth.presentation"
        compileSdk = 36
        minSdk = 26
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.presentation)
                implementation(projects.core.designSystem)
                implementation(projects.feature.auth.domain)

                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.lifecycle)
                implementation(libs.bundles.koin.core.bundle)
                implementation(libs.bundles.koin.compose)
                implementation(libs.jetbrains.kotlinx.collections.immutable)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.auth.presentation"
}
