package karya.servers.executor.di

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.components.FusedDataQueueComponent
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.usecase.ExecutorService

@ExecutorScope
@Component(
  dependencies = [
    FusedDataQueueComponent::class,
    FusedDataRepoComponent::class
  ]
)
interface ExecutorComponent {

  val executorService: ExecutorService

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(config: ExecutorConfig): Builder

    fun fusedQueueComponent(fusedDataQueueComponent: FusedDataQueueComponent): Builder

    fun fusedRepoComponent(fusedDataRepoComponent: FusedDataRepoComponent): Builder

    fun build(): ExecutorComponent
  }
}
