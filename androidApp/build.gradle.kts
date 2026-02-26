plugins {
    alias(libs.plugins.convention.android.application.compose)
    alias(libs.plugins.googleServices)
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.koin.android)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
}