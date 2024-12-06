plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {
  implementation(project(Modules.CORE))

  // Chained
  implementation(project(Modules.Servers.SERVER))
  implementation(project(Modules.Data.FUSED))

  // Email
  implementation(Libs.Javax.API)
  implementation(Libs.Javax.IMPL)

  // Slack
  implementation(Libs.SLACK_API)
  implementation(Libs.Kotlinx.JSON_SERIALIZATION)

  // Rest API
  implementation(Libs.Ktor.KOTLINX)
  implementation(Libs.Ktor.Client.CORE)
  implementation(Libs.Ktor.Client.CONTENT_NEGOTIATION)
  implementation(Libs.Ktor.Client.CIO)

  // Kafka
  implementation(Libs.KAFKA_CLIENT)

  implementation(Libs.Dagger.LIBRARY)
  kapt(Libs.Dagger.COMPILER)
}
