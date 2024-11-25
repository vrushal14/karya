package karya.servers.server.app.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import javax.inject.Inject

class FeatureManager
@Inject
constructor() {
  @OptIn(ExperimentalSerializationApi::class)
  fun Application.wireFeatures() {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
          allowStructuredMapKeys = true
          namingStrategy = JsonNamingStrategy.SnakeCase
          useAlternativeNames = true
          encodeDefaults = true
        },
      )
    }

    install(CallLogging) {
      format { call ->
        val status = call.response.status()
        val httpMethod = call.request.httpMethod.value
        val endpoint = call.request.uri
        "$status - $httpMethod $endpoint"
      }
    }
  }
}
