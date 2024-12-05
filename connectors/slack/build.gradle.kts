plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {
  implementation(project(Modules.CORE))

  implementation(Libs.SLACK_API)
  implementation(Libs.Kotlinx.JSON_SERIALIZATION)

  implementation(Libs.Dagger.LIBRARY)
  kapt(Libs.Dagger.COMPILER)
}
