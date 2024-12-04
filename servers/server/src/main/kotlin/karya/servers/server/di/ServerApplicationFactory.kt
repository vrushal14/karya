package karya.servers.server.di

import karya.data.fused.di.factories.FusedDataLocksComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.server.configs.ServerConfig

/**
 * Factory object for creating the server application.
 */
object ServerApplicationFactory {

  /**
   * Creates the server application using the provided configuration.
   *
   * @param config The server configuration.
   * @return The created server application.
   */
  fun create(config: ServerConfig) = DaggerServerComponent
    .builder()
    .serverConfig(config)
    .fusedDataRepoComponent(FusedDataRepoComponentFactory.build(config.repoConfig))
    .fusedDataLocksComponent(FusedDataLocksComponentFactory.build(config.locksConfig))
    .build()
    .serverApplication
}
