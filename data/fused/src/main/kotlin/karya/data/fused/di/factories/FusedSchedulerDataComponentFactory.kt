package karya.data.fused.di.factories

import karya.core.configs.LocksConfig
import karya.core.configs.QueueConfig
import karya.core.configs.RepoConfig
import karya.data.fused.di.components.DaggerFusedSchedulerDataComponent

object FusedSchedulerDataComponentFactory {
  fun build(repoConfig: RepoConfig, locksConfig: LocksConfig, queueConfig: QueueConfig) =
    DaggerFusedSchedulerDataComponent
      .builder()
      .repoConfig(repoConfig)
      .locksConfig(locksConfig)
      .queueConfig(queueConfig)
      .build()
}
