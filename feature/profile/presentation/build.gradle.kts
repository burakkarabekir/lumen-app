plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.profile.presentation"
        compileSdk = 36
        minSdk = 26
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.presentation)
                implementation(projects.core.designSystem)
                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.lifecycle)
                implementation(libs.bundles.koin.core.bundle)
                implementation(libs.bundles.koin.compose)
                implementation(libs.jetbrains.kotlinx.collections.immutable)

                implementation(libs.jetbrains.compose.material3)
                implementation(libs.jetbrains.compose.material.icons.core)
                implementation(libs.jetbrains.compose.material.icons.extended)
            }
        }
    }
}
