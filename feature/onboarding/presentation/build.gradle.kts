plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    android {
        namespace = "com.bksd.onboarding.presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
}

compose.resources {
    packageOfResClass = "com.bksd.onboarding.presentation"
}
