package karya.servers.scheduler.di

import karya.data.fused.di.DaggerFusedDataComponent
import karya.data.fused.locks.LocksConfig
import karya.data.fused.repos.RepoConfig
import karya.servers.scheduler.configs.SchedulerConfig

object SchedulerApplicationFactory {

  fun create(schedulerConfig: SchedulerConfig) =
    DaggerSchedulerComponent.builder()
      .fusedDataComponent(provideFusedRepoComponent(schedulerConfig.repoConfig, schedulerConfig.locksConfig))
      .config(schedulerConfig)
      .build()
      .schedulerApplication

  private fun provideFusedRepoComponent(repoConfig: RepoConfig, locksConfig : LocksConfig) =
    DaggerFusedDataComponent.builder()
      .repoConfig(repoConfig)
      .locksConfig(locksConfig)
      .build()

}