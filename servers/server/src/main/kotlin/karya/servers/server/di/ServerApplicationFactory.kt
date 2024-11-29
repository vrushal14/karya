package karya.servers.server.di


import karya.data.fused.di.factories.FusedDataLocksComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.server.configs.ServerConfig

object ServerApplicationFactory {
  fun create(config: ServerConfig) = DaggerServerComponent
    .builder()
    .serverConfig(config)
    .fusedDataRepoComponent(FusedDataRepoComponentFactory.build(config.repoConfig))
    .fusedDataLocksComponent(FusedDataLocksComponentFactory.build(config.locksConfig))
    .build()
    .serverApplication
}
