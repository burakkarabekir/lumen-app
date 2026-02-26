plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bksd.moment.data"
        compileSdk = 36
        minSdk = 26
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.data)
                implementation(projects.core.domain)
                implementation(projects.feature.moment.domain)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.insert.koin.koin.core)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
