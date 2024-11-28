package karya.data.fused.di.factories

import karya.core.configs.QueueConfig
import karya.core.configs.RepoConfig
import karya.data.fused.di.components.DaggerFusedExecutorDataComponent

object FusedExecutorDataComponentFactory {
  fun build(repoConfig: RepoConfig, queueConfig: QueueConfig) =
    DaggerFusedExecutorDataComponent.builder()
      .repoConfig(repoConfig)
      .queueConfig(queueConfig)
      .build()
}
