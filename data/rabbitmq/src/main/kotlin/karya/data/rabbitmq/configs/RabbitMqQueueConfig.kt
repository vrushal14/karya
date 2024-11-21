package karya.data.rabbitmq.configs

import karya.core.configs.QueueConfig
import karya.core.utils.PropsReader.readValue

data class RabbitMqQueueConfig(
	val username: String,
	val password: String,
	val virtualHost: String,
	val hostname: String,
	val port: Int,
) : QueueConfig(RABBITMQ_IDENTIFIER) {
	companion object {
		const val RABBITMQ_IDENTIFIER = "rabbitmq"
	}

	constructor(props: Map<*, *>) : this(
		username = props.readValue("username"),
		password = props.readValue("password"),
		virtualHost = props.readValue("virtualHost"),
		hostname = props.readValue("hostname"),
		port = props.readValue("port"),
	)
}
