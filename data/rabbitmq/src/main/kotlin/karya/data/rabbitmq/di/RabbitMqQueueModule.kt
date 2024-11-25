package karya.data.rabbitmq.di

import dagger.Binds
import dagger.Module
import karya.core.queues.QueueClient
import karya.data.rabbitmq.RabbitMqQueueClient
import javax.inject.Singleton

@Module
abstract class RabbitMqQueueModule {

    @Binds
    @Singleton
    abstract fun provideRabbitMqQueueClient(client: RabbitMqQueueClient): QueueClient
}
