package karya.data.fused

import karya.core.configs.QueueConfig
import karya.core.utils.getSection
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import karya.data.rabbitmq.configs.RabbitMqQueueConfig.Companion.RABBITMQ_IDENTIFIER

object QueueSelector {

  fun get(filePath: String): QueueConfig {
    val section: Map<String, *> = getSection(filePath, "queue")
    val properties = section["properties"] as Map<*, *>

    return when (val provider = section["provider"]) {
      RABBITMQ_IDENTIFIER -> RabbitMqQueueConfig(properties)

      else -> throw UnknownProviderException("queue", provider.toString())
    }
  }
}
