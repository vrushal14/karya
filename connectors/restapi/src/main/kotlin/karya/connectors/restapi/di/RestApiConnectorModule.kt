package karya.connectors.restapi.di

import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import karya.connectors.restapi.RestApiConnector
import karya.connectors.restapi.configs.RestApiConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.action.Action
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import javax.inject.Singleton

@Module
class RestApiConnectorModule {
  @Provides
  @Singleton
  fun provideRestApiConnector(httpClient: HttpClient): Connector<Action.RestApiRequest> = RestApiConnector(httpClient)

  @Provides
  @Singleton
  fun provideHttpClient(config: RestApiConnectorConfig): HttpClient =
    HttpClient(CIO) {
      engine {
        endpoint {
          keepAliveTime = config.keepAliveTime
          connectTimeout = config.connectionTimeout
          connectAttempts = config.connectionAttempt
        }
      }
      defaultRequest {
        contentType(ContentType.Application.Json)
        header("connection", "keep-alive")
      }
      expectSuccess = false

      install(ContentNegotiation) { json(configureJson()) }
    }

  @OptIn(ExperimentalSerializationApi::class)
  private fun configureJson(): Json =
    Json {
      isLenient = true
      ignoreUnknownKeys = true
      encodeDefaults = true
      useAlternativeNames = true
      allowStructuredMapKeys = true
      namingStrategy = JsonNamingStrategy.SnakeCase
    }
}
