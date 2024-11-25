plugins {
    id(Plugins.Kotlin.JVM) version Plugins.Kotlin.VERSION apply false
    id(Plugins.Detekt.LIBRARY) version Plugins.Detekt.VERSION
}

allprojects {
    apply(plugin = Plugins.Detekt.LIBRARY)
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    detekt {
        toolVersion = Plugins.Detekt.VERSION
        config.setFrom(files("$rootDir/detekt.yml"))
        buildUponDefaultConfig = false
    }
}

subprojects {
    group = "karya"
    version = "0.0.1"

    apply(plugin = Plugins.Kotlin.JVM)

    dependencies {
        detektPlugins(Plugins.Detekt.FORMATTING)
    }
}

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektFormat") {
    description = "Formats whole project."
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")

    dependencies {
        detektPlugins(Plugins.Detekt.FORMATTING)
    }
}

ext["kapt.includeCompileClasspath"] = false
