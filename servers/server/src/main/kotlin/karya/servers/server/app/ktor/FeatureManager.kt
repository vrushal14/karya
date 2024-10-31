package karya.servers.server.app.ktor

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import javax.inject.Inject

class FeatureManager
@Inject
constructor() {
  fun Application.wireFeatures() {

    install(ContentNegotiation) {
      jackson {
        registerKotlinModule()
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        propertyNamingStrategy = SNAKE_CASE
      }
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