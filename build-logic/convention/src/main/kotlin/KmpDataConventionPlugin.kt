import com.bksd.lumen.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpDataConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.bksd.convention.kmp.library")
            }

            dependencies {
                "commonMainImplementation"(project(":core:data"))
                "commonMainImplementation"(project(":core:domain"))

                "commonMainImplementation"(libs.findLibrary("kotlinx-coroutines-core").get())
                "commonMainImplementation"(libs.findLibrary("koin-core").get())
            }
        }
    }
}
