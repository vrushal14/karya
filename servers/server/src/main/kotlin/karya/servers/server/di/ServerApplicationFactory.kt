package karya.servers.server.di

import karya.data.fused.repos.RepoConfig
import karya.data.fused.locks.LocksConfig
import karya.data.fused.di.DaggerFusedDataComponent

object ServerApplicationFactory {

  fun create(repoConfig: RepoConfig, locksConfig: LocksConfig) =
    DaggerServerComponent.builder()
      .fusedDataComponent(provideFusedRepoComponent(repoConfig, locksConfig))
      .build()
      .serverApplication

  private fun provideFusedRepoComponent(repoConfig: RepoConfig, locksConfig : LocksConfig) =
    DaggerFusedDataComponent.builder()
      .repoConfig(repoConfig)
      .locksConfig(locksConfig)
      .build()
}