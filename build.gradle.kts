plugins {
	id(Plugins.Kotlin.JVM) version Plugins.Kotlin.VERSION apply false
	id(Plugins.Ktlint.KTLINT) version Plugins.Ktlint.VERSION
}

allprojects {
	apply(plugin = Plugins.Ktlint.KTLINT)
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
