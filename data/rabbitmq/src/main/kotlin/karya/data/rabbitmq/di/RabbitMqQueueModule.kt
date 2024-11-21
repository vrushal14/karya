package karya.data.rabbitmq.di

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import dagger.Module
import dagger.Provides
import karya.core.queues.QueueClient
import karya.data.rabbitmq.RabbitMqQueueClient
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import javax.inject.Singleton

@Module
class RabbitMqQueueModule {
	@Provides
	@Singleton
	fun provideRabbitMqQueueClient(
		connection: Connection,
		channel: Channel,
	): QueueClient = RabbitMqQueueClient(channel, connection)

	@Provides
	@Singleton
	fun provideConnection(config: RabbitMqQueueConfig): Connection {
		val factory = ConnectionFactory()
		factory.username = config.username
		factory.password = config.password
		factory.virtualHost = config.virtualHost
		factory.host = config.hostname
		factory.port = config.port

		return factory.newConnection("karya-rabbitMq")
	}

	@Provides
	@Singleton
	fun provideChannel(connection: Connection): Channel = connection.createChannel()
}
