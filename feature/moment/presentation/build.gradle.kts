plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    android {
        namespace = "com.bksd.moment.presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                api(projects.feature.moment.domain)
                api(projects.feature.journal.domain)
                implementation(projects.feature.auth.domain)
                implementation(libs.bundles.jetbrains.adaptive)
                implementation(libs.jetbrains.compose.backhandler)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.moment.presentation"
}
