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

  object Detekt {
    const val LIBRARY = "io.gitlab.arturbosch.detekt"
    const val VERSION = "1.23.4"
    const val FORMATTING = "$LIBRARY:detekt-formatting:$VERSION"
  }
}
