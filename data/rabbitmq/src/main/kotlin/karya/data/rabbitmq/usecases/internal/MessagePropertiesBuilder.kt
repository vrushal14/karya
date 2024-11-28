package karya.data.rabbitmq.usecases.internal

import com.rabbitmq.client.AMQP.BasicProperties
import javax.inject.Inject

class MessagePropertiesBuilder
@Inject
constructor() {

  companion object {
    private const val CONTENT_TYPE = "application/json"
    private const val DELIVERY_MODE = 2 // persistent
    private const val PRIORITY = 1
  }

  fun build(): BasicProperties {
    return BasicProperties
      .Builder()
      .contentType(CONTENT_TYPE)
      .deliveryMode(DELIVERY_MODE) // persistent
      .priority(PRIORITY)
      .build()
  }
}
