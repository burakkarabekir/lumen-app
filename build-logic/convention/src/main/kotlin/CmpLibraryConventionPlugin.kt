import com.bksd.lumen.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.bksd.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(
                    libs.findLibrary("jetbrains-compose-material-icons-core").get()
                )
                "commonMainImplementation"(libs.findLibrary("components-resources").get())
                "commonMainImplementation"(
                    libs.findLibrary("jetbrains-compose-ui-tooling-preview").get()
                )

                "androidMainImplementation"(libs.findLibrary("jetbrains-compose-ui-tooling").get())
            }
        }
    }
}