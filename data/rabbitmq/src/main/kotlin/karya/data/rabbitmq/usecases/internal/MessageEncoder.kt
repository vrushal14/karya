package karya.data.rabbitmq.usecases.internal

import karya.core.queues.entities.QueueMessage
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessageEncoder
@Inject
constructor(
  private val json: Json
) {

  fun encode(message: QueueMessage): ByteArray {
    return json.encodeToString(QueueMessage.serializer(), message)
      .toByteArray(Charsets.UTF_8)
  }

  fun decode(message: ByteArray?): QueueMessage {
    val messageJson = message?.toString(Charsets.UTF_8)
      ?: throw IllegalArgumentException("Message body is null")
    return json.decodeFromString<QueueMessage>(messageJson)
  }
}
