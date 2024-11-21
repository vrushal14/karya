package karya.servers.server.di

import karya.core.configs.LocksConfig
import karya.core.configs.RepoConfig
import karya.data.fused.di.components.DaggerFusedServerDataComponent

object ServerApplicationFactory {

  fun create(repoConfig: RepoConfig, locksConfig: LocksConfig) =
    DaggerServerComponent.builder()
      .fusedServerDataComponent(provideFusedRepoComponent(repoConfig, locksConfig))
      .build()
      .serverApplication

  private fun provideFusedRepoComponent(repoConfig: RepoConfig, locksConfig : LocksConfig) =
    DaggerFusedServerDataComponent.builder()
      .repoConfig(repoConfig)
      .locksConfig(locksConfig)
      .build()
}