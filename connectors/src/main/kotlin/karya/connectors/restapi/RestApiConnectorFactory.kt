package karya.connectors.restapi

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import karya.core.actors.Connector
import karya.core.entities.Action
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

/**
 * Factory object for creating instances of RestApiConnector.
 */
object RestApiConnectorFactory {

  /**
   * Builds a RestApiConnector using the provided configuration map.
   *
   * @param configMap The map containing configuration values.
   * @return An instance of RestApiConnector.
   */
  fun build(configMap: Map<*, *>): Connector<Action.RestApiRequest> {
    val config = RestApiConnectorConfig(configMap)
    val httpClient = provideHttpClient(config)
    return RestApiConnector(httpClient)
  }

  /**
   * Provides an HttpClient using the provided RestApiConnectorConfig.
   *
   * @param config The configuration for the RestApiConnector.
   * @return An instance of HttpClient.
   */
  @OptIn(ExperimentalSerializationApi::class)
  fun provideHttpClient(config: RestApiConnectorConfig): HttpClient =
    HttpClient(CIO) {
      engine {
        endpoint {
          keepAliveTime = config.keepAliveTime
          connectTimeout = config.connectionTimeout
          connectAttempts = config.connectionAttempts
        }
      }
      defaultRequest {
        contentType(ContentType.Application.Json)
        header("connection", "keep-alive")
      }
      expectSuccess = false

      install(ContentNegotiation) {
        json(Json {
          isLenient = true
          ignoreUnknownKeys = true
          encodeDefaults = true
          useAlternativeNames = true
          allowStructuredMapKeys = true
          namingStrategy = JsonNamingStrategy.SnakeCase
        })
      }
    }

}
