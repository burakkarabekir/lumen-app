plugins {
    alias(libs.plugins.convention.cmp.application)
}

kotlin {
    android {
        namespace = "com.bksd.lumen.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.presentation)
            implementation(projects.core.designSystem)

            implementation(projects.feature.onboarding.presentation)
            implementation(projects.feature.auth.data)
            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.presentation)

            implementation(projects.feature.journal.data)
            implementation(projects.feature.journal.domain)
            implementation(projects.feature.journal.presentation)

            implementation(projects.feature.moment.data)
            implementation(projects.feature.moment.domain)
            implementation(projects.feature.moment.presentation)

            implementation(projects.feature.insights.presentation)

            implementation(projects.feature.profile.data)
            implementation(projects.feature.profile.domain)
            implementation(projects.feature.profile.presentation)

            implementation(projects.feature.paywall.domain)
            implementation(projects.feature.paywall.presentation)

            implementation(libs.bundles.compose)

            implementation(libs.bundles.lifecycle)
            implementation(libs.bundles.navigation3)

            implementation(libs.bundles.koin.compose)

            implementation(libs.jetbrains.kotlinx.collections.immutable)
            implementation(libs.kotzilla.sdk.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    packageOfResClass = "com.bksd.lumen"
}
