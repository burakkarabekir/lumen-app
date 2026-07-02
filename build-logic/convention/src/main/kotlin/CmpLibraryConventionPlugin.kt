import com.bksd.lumen.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class CmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.bksd.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                stabilityConfigurationFiles.add(
                    rootProject.layout.projectDirectory.file("compose_stability_config.conf")
                )
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(
                    libs.findLibrary("jetbrains-compose-material-icons-core").get()
                )
                "commonMainImplementation"(
                    libs.findLibrary("jetbrains-compose-material-icons-extended").get()
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