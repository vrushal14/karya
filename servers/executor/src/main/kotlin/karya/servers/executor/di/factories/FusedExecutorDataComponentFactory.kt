package karya.servers.executor.di.factories

import karya.data.fused.di.components.DaggerFusedExecutorDataComponent
import karya.servers.executor.configs.ExecutorConfig

object FusedExecutorDataComponentFactory {
  fun build(config: ExecutorConfig) =
    DaggerFusedExecutorDataComponent.builder()
      .repoConfig(config.repoConfig)
      .queueConfig(config.queueConfig)
      .build()
}
