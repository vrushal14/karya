package karya.client.di

import karya.client.configs.KaryaClientConfig

/**
 * Factory object for creating instances of KaryaClient.
 */
object KaryaClientFactory {

  /**
   * Creates an instance of KaryaClient using the provided configuration.
   *
   * @param config The configuration object for the KaryaClient.
   * @return The created KaryaClient instance.
   */
  fun create(config: KaryaClientConfig) =
    DaggerKaryaClientComponent
      .builder()
      .clientConfig(config)
      .build()
      .client
}
