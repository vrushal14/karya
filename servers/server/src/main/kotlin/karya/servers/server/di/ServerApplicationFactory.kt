package karya.servers.server.di

import karya.core.configs.LocksConfig
import karya.core.configs.RepoConfig
import karya.data.fused.di.factories.FusedDataLocksComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory

object ServerApplicationFactory {
  fun create(repoConfig: RepoConfig, locksConfig: LocksConfig) = DaggerServerComponent
    .builder()
    .fusedDataRepoComponent(FusedDataRepoComponentFactory.build(repoConfig))
    .fusedDataLocksComponent(FusedDataLocksComponentFactory.build(locksConfig))
    .build()
    .serverApplication
}
