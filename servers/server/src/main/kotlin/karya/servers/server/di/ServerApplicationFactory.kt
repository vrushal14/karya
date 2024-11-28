package karya.servers.server.di

import karya.core.configs.LocksConfig
import karya.core.configs.RepoConfig
import karya.data.fused.di.factories.FusedServerDataComponentFactory

object ServerApplicationFactory {
  fun create(repoConfig: RepoConfig, locksConfig: LocksConfig) = DaggerServerComponent
    .builder()
    .fusedServerDataComponent(FusedServerDataComponentFactory.build(repoConfig, locksConfig))
    .build()
    .serverApplication
}
