package karya.servers.executor.di

import karya.servers.executor.configs.ExecutorConfig

/**
 * Factory object for creating instances of [ExecutorService].
 */
object ExecutorServiceFactory {

  /**
   * Builds an instance of [ExecutorService] using the provided configuration.
   *
   * @param config The configuration for the executor service.
   * @return An instance of [ExecutorService].
   */
  fun build(config: ExecutorConfig) = DaggerExecutorComponent.builder()
    .config(config)
    .fusedRepoComponent(config.fusedDataRepoComponent)
    .fusedQueueComponent(config.fusedDataQueueComponent)
    .build()
    .executorService
}
