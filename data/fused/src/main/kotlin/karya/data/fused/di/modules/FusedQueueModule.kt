package karya.data.fused.di.modules

import dagger.Module
import dagger.Provides
import karya.core.configs.QueueConfig
import karya.data.fused.exceptions.FusedDataException.UnknownProviderException
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import karya.data.rabbitmq.di.RabbitMqQueueClientFactory
import javax.inject.Singleton

@Module
class FusedQueueModule {
  @Provides
  @Singleton
  fun provideQueueClient(queueConfig: QueueConfig) =
    when (queueConfig) {
      is RabbitMqQueueConfig -> RabbitMqQueueClientFactory.build(queueConfig)

      else -> throw UnknownProviderException("queue", queueConfig.provider)
    }
}
