package karya.data.rabbitmq.di

import karya.data.rabbitmq.configs.RabbitMqQueueConfig

object RabbitMqQueueClientFactory {
    fun build(config: RabbitMqQueueConfig) =
        DaggerRabbitMqQueueComponent
            .builder()
            .rabbitMqConfig(config)
            .build()
            .queueClient
}
