plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.profile.data"
        compileSdk = 36
        minSdk = 26
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.data)
                implementation(projects.core.domain)
                implementation(projects.feature.profile.domain)
                
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.bundles.koin.core.bundle)
                implementation(libs.bundles.datastore)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.insert.koin.koin.android)
            }
        }
    }
}
