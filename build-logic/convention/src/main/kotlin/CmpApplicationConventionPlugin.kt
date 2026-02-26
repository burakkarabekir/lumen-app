import com.bksd.lumen.convention.configureAndroidLibraryTarget
import com.bksd.lumen.convention.configureIosTargets
import com.bksd.lumen.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureAndroidLibraryTarget()
            configureIosTargets()

            dependencies {
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-runtime").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())

                // CMP 1.10.0+: Resources and preview tooling are now separate modules
                "commonMainImplementation"(libs.findLibrary("components-resources").get())
                "commonMainImplementation"(
                    libs.findLibrary("jetbrains-compose-ui-tooling-preview").get()
                )

                // Single-variant model: use androidMainImplementation instead of debugImplementation
                "androidMainImplementation"(libs.findLibrary("jetbrains-compose-ui-tooling").get())
            }
        }
    }
}