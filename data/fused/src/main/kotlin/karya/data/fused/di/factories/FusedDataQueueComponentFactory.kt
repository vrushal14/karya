package karya.data.fused.di.factories

import karya.core.configs.QueueConfig
import karya.data.fused.di.components.DaggerFusedDataQueueComponent

/**
 * Factory object for creating instances of FusedDataQueueComponent.
 */
object FusedDataQueueComponentFactory {

  /**
   * Builds a FusedDataQueueComponent using the provided queue configuration.
   *
   * @param queueConfig The configuration for the queue.
   * @return An instance of FusedDataQueueComponent.
   */
  fun build(queueConfig: QueueConfig) =
    DaggerFusedDataQueueComponent.builder()
      .queueConfig(queueConfig)
      .build()
}
