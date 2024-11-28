package karya.servers.executor.di.factories

import karya.data.fused.di.factories.FusedExecutorDataComponentFactory
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.di.DaggerExecutorComponent

object ExecutorServiceFactory {
  fun build(config: ExecutorConfig) = DaggerExecutorComponent.builder()
    .config(config)
    .fusedDataComponent(fusedExecutorDataComponent(config))
    .build()
    .executorService

  private fun fusedExecutorDataComponent(config: ExecutorConfig) =
    FusedExecutorDataComponentFactory.build(config.repoConfig, config.queueConfig)
}
