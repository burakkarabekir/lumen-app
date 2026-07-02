plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    android {
        namespace = "com.bksd.insights.presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.feature.insights.domain)
                implementation(projects.feature.reflection.domain)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.insights.presentation"
}
