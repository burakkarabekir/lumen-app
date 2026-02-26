import com.bksd.lumen.convention.pathToPackageName
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.Properties

class BuildKonfigConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            extensions.configure<BuildKonfigExtension> {
                packageName = target.pathToPackageName()
                defaultConfigs {
                    val configProperties = Properties()
                    val configFile = rootProject.file("config.properties")

                    if (configFile.exists()) {
                        FileInputStream(configFile).use { configProperties.load(it) }
                    }

                    val apiKey = configProperties.getProperty("API_KEY") ?: "-"
                        /*?: throw IllegalStateException(
                            "Missing API_KEY property in config.properties"
                        )*/
                    buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
                }
            }
        }
    }
}