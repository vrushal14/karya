package karya.data.rabbitmq.di

import dagger.BindsInstance
import dagger.Component
import karya.core.queues.QueueClient
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    RabbitMqQueueModule::class,
    RabbitMqQueueUtilsModule::class,
  ],
)
interface RabbitMqQueueComponent {
  val queueClient: QueueClient

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun rabbitMqConfig(rabbitMqQueueConfig: RabbitMqQueueConfig): Builder

    fun build(): RabbitMqQueueComponent
  }
}
