package karya.servers.executor.di.factories

import karya.data.fused.di.factories.FusedDataQueueComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.di.DaggerExecutorComponent

object ExecutorServiceFactory {
  fun build(config: ExecutorConfig) = DaggerExecutorComponent.builder()
    .config(config)
    .fusedRepoComponent(FusedDataRepoComponentFactory.build(config.repoConfig))
    .fusedQueueComponent(FusedDataQueueComponentFactory.build(config.queueConfig))
    .build()
    .executorService
}
