import com.android.build.api.dsl.ApplicationExtension
import com.bksd.lumen.convention.configureKotlinAndroid
import com.bksd.lumen.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            val keystorePropertiesFile = target.rootProject.file("keystore.properties")
            val hasKeystore = keystorePropertiesFile.exists()
            val keystoreProperties = Properties().apply {
                if (hasKeystore) FileInputStream(keystorePropertiesFile).use { load(it) }
            }

            extensions.configure<ApplicationExtension> {
                namespace = "com.bksd.lumen"
                compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()

                defaultConfig {
                    applicationId = libs.findVersion("applicationId").get().toString()
                    targetSdk = libs.findVersion("android-targetSdk").get().toString().toInt()

                    val semanticVersion = libs.findVersion("versionName").get().toString()
                    val versionParts = semanticVersion.split(".")
                    val major = versionParts.getOrNull(0)?.toIntOrNull() ?: 0
                    val minor = versionParts.getOrNull(1)?.toIntOrNull() ?: 0
                    val patch = versionParts.getOrNull(2)?.toIntOrNull() ?: 0

                    versionCode = major * 10000 + minor * 100 + patch
                    versionName = semanticVersion
                }

                if (hasKeystore) {
                    signingConfigs {
                        create("release") {
                            storeFile = target.rootProject.file(keystoreProperties.getProperty("storeFile"))
                            storePassword = keystoreProperties.getProperty("storePassword")
                            keyAlias = keystoreProperties.getProperty("keyAlias")
                            keyPassword = keystoreProperties.getProperty("keyPassword")
                        }
                    }
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                        signingConfig = if (hasKeystore) {
                            signingConfigs.getByName("release")
                        } else {
                            signingConfigs.getByName("debug")
                        }
                    }
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
