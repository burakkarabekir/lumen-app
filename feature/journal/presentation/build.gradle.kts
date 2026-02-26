plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.journal.presentation"
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
                api(projects.feature.journal.domain)
                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.jetbrains.adaptive)
                implementation(libs.bundles.lifecycle)
                implementation(libs.bundles.koin.core.bundle)
                implementation(libs.bundles.koin.compose)
                implementation(libs.jetbrains.compose.backhandler)
                implementation(libs.kotlinx.datetime)

                implementation(libs.jetbrains.kotlinx.collections.immutable)
                implementation(libs.moko.permissions)
                implementation(libs.moko.permissions.compose)
                implementation(libs.moko.permissions.microphone)
            }
        }
    }
}


compose.resources {
    packageOfResClass = "com.bksd.journal.presentation"
}