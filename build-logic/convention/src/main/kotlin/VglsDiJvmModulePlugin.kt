import com.vgleadsheets.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class VglsDiJvmModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
            }

            dependencies {
                add("implementation", project(":core:common:di"))

                "kapt"(libs.findLibrary("dagger.compiler").get())
                "kapt"(libs.findLibrary("dagger.android.processor").get())
            }
        }
    }
}
