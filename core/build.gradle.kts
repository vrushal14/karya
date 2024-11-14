plugins {
  kotlin(Plugins.Serialization.KOTLINX) version Plugins.Serialization.VERSION
}

dependencies {
  implementation(Libs.Kotlinx.JSON_SERIALIZATION)
}