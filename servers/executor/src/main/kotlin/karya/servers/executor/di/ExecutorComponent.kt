package karya.servers.executor.di

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.components.FusedExecutorDataComponent
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.usecase.external.ExecutorService

@ExecutorScope
@Component(
  dependencies = [
    FusedExecutorDataComponent::class
  ]
)
interface ExecutorComponent {

  val executorService: ExecutorService

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(config: ExecutorConfig): Builder

    fun fusedDataComponent(fusedExecutorDataComponent: FusedExecutorDataComponent): Builder

    fun build(): ExecutorComponent
  }
}
