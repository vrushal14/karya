plugins {
    id(Plugins.Kotlin.JVM) version Plugins.Kotlin.VERSION apply false
}

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects {
    group = "karya"
    version = "0.0.1"

    apply(plugin = Plugins.Kotlin.JVM)
}

