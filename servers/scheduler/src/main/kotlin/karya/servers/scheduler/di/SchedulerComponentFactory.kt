package karya.servers.scheduler.di

import karya.data.fused.di.DaggerFusedDataComponent
import karya.data.fused.locks.LocksConfig
import karya.data.fused.repos.RepoConfig
import karya.servers.scheduler.configs.SchedulerConfig

object SchedulerComponentFactory {

  fun create(repoConfig: RepoConfig, locksConfig: LocksConfig, schedulerConfig: SchedulerConfig) =
    DaggerSchedulerComponent.builder()
      .fusedDataComponent(provideFusedRepoComponent(repoConfig, locksConfig))
      .config(schedulerConfig)
      .build()

  private fun provideFusedRepoComponent(repoConfig: RepoConfig, locksConfig : LocksConfig) =
    DaggerFusedDataComponent.builder()
      .repoConfig(repoConfig)
      .locksConfig(locksConfig)
      .build()

}