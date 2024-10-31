package karya.client.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dagger.Module
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import karya.client.configs.KaryaClientConfig
import karya.client.ktor.KaryaClientImpl
import karya.core.actors.Client
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
  fun provideKaryaClient(httpClient: HttpClient, objectMapper: ObjectMapper) : Client =
    KaryaClientImpl(httpClient, objectMapper)

  @Provides
  @Singleton
  fun provideHttpClient(config: KaryaClientConfig) : HttpClient = HttpClient(CIO) {
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
      header("connection","keep-alive")
    }
    expectSuccess = false

    install(ContentNegotiation) { jackson { configureDefaults() } }
  }

  @Provides
  @Singleton
  fun provideObjectMapper() : ObjectMapper =
    jacksonObjectMapper().apply { configureDefaults() }

  private fun ObjectMapper.configureDefaults() {
    configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    propertyNamingStrategy = SNAKE_CASE

    registerKotlinModule()
  }

}