object Plugins {
	object Kotlin {
		private const val KOTLIN_PREFIX = "org.jetbrains.kotlin"
		const val JVM = "$KOTLIN_PREFIX.jvm"
		const val KAPT = "kotlin-kapt"
		const val VERSION = "2.0.21"
	}

	object Serialization {
		const val KOTLINX = "plugin.serialization"
		const val VERSION = "2.0.20"
	}

	object Ktlint {
		const val KTLINT = "org.jlleitschuh.gradle.ktlint"
		const val VERSION = "12.1.1"
	}
}
