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
                packageName.set(target.pathToPackageName())
                defaultConfigs {
                    val configProperties = Properties()
                    val configFile = rootProject.file("config.properties")

                    if (configFile.exists()) {
                        FileInputStream(configFile).use { configProperties.load(it) }
                    }

                    val supabaseUrl = configProperties.getProperty("SUPABASE_URL") ?: ""
                    val supabaseAnonKey = configProperties.getProperty("SUPABASE_ANON_KEY") ?: ""
                    buildConfigField(FieldSpec.Type.STRING, "SUPABASE_URL", supabaseUrl)
                    buildConfigField(FieldSpec.Type.STRING, "SUPABASE_ANON_KEY", supabaseAnonKey)
                }
            }
        }
    }
}