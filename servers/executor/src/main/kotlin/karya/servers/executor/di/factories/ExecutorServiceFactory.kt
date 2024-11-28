package karya.servers.executor.di.factories

import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.di.DaggerExecutorComponent

object ExecutorServiceFactory {
  fun build(config: ExecutorConfig) = DaggerExecutorComponent.builder()
    .config(config)
    .fusedRepoComponent(config.fusedDataRepoComponent)
    .fusedQueueComponent(config.fusedDataQueueComponent)
    .build()
    .executorService
}
