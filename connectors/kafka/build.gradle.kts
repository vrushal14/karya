plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {
  implementation(project(Modules.CORE))

  implementation(Libs.KAFKA_CLIENT)

  implementation(Libs.Dagger.LIBRARY)
  kapt(Libs.Dagger.COMPILER)
}
