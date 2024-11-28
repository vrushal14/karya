package karya.data.fused.di.factories

import karya.core.configs.QueueConfig
import karya.data.fused.di.components.DaggerFusedDataQueueComponent

object FusedDataQueueComponentFactory {
  fun build(queueConfig: QueueConfig) =
    DaggerFusedDataQueueComponent.builder()
      .queueConfig(queueConfig)
      .build()
}
