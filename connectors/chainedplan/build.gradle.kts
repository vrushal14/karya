plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {
  implementation(project(Modules.CORE))
  implementation(project(Modules.Servers.SERVER))
  implementation(project(Modules.Data.FUSED))

  implementation(Libs.Dagger.LIBRARY)
  kapt(Libs.Dagger.COMPILER)
}
