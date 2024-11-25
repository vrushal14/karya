package karya.client.di

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
import karya.client.configs.KaryaClientConfig
import karya.client.ktor.KaryaClientImpl
import karya.core.actors.Client
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import javax.inject.Singleton

@Module
class KaryaClientModule {
  companion object {
    private const val KEEP_ALIVE_TIME = 5000L
    private const val CONNECTION_TIMEOUT = 5000L
    private const val CONNECTION_ATTEMPT = 5
  }

  @Provides
  @Singleton
  fun provideKaryaClient(httpClient: HttpClient): Client = KaryaClientImpl(httpClient, configureJson())

  @Provides
  @Singleton
  fun provideHttpClient(config: KaryaClientConfig): HttpClient =
    HttpClient(CIO) {
      engine {
        endpoint {
          keepAliveTime = KEEP_ALIVE_TIME
          connectTimeout = CONNECTION_TIMEOUT
          connectAttempts = CONNECTION_ATTEMPT
        }
      }
      defaultRequest {
        url.protocol = config.protocol
        url.host = config.host
        url.port = config.port
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
