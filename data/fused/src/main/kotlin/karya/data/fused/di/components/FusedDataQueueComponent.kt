package karya.data.fused.di.components

import dagger.BindsInstance
import dagger.Component
import karya.core.configs.QueueConfig
import karya.core.queues.QueueClient
import karya.data.fused.di.modules.FusedQueueModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    FusedQueueModule::class,
  ],
)
interface FusedDataQueueComponent {

  val queueClient: QueueClient

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun queueConfig(queueConfig: QueueConfig): Builder

    fun build(): FusedDataQueueComponent
  }
}
