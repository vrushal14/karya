package karya.client.di

import dagger.Module
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import karya.client.configs.KaryaClientConfig
import karya.core.entities.action.http.Protocol
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import javax.inject.Singleton

@Module
class KaryaClientUtilsModule {

  @Provides
  @Singleton
  fun provideHttpClient(config: KaryaClientConfig): HttpClient =
    HttpClient(CIO) {
      engine {
        endpoint {
          keepAliveTime = config.keepAliveTime
          connectTimeout = config.connectionTimeout
          connectAttempts = config.connectionAttempts
        }
      }
      defaultRequest {
        url.protocol = mapProtocol(config.protocol)
        url.host = config.host
        url.port = config.port
        contentType(ContentType.Application.Json)
        header("connection", "keep-alive")
      }
      expectSuccess = false

      install(ContentNegotiation) { json(configureJson()) }
    }

  @Provides
  @Singleton
  @OptIn(ExperimentalSerializationApi::class)
  fun configureJson(): Json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    useAlternativeNames = true
    allowStructuredMapKeys = true
    namingStrategy = JsonNamingStrategy.SnakeCase
  }

  private fun mapProtocol(protocol: Protocol) = when (protocol) {
    Protocol.HTTP -> URLProtocol.HTTP
    Protocol.HTTPS -> URLProtocol.HTTPS
  }
}
