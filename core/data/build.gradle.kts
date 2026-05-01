plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.buildkonfig)
}

kotlin {
    android {
        namespace = "com.bksd.core.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)

                implementation(libs.bundles.ktor.common)
                implementation(libs.kermit)
                implementation(libs.koin.core)

                implementation(libs.bundles.datastore)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)

                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
                implementation(libs.firebase.storage)

                implementation(projects.feature.auth.domain)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.google.firebase.auth)
                implementation(libs.kotlinx.coroutines.play.services)
                implementation(libs.play.services.location)
                implementation(libs.androidx.credentials)
                implementation(libs.androidx.credentials.play.services.auth)
                implementation(libs.googleid)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}