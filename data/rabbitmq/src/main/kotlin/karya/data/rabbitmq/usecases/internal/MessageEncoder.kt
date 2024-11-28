package karya.data.rabbitmq.usecases.internal

import karya.core.queues.entities.ExecutorMessage
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessageEncoder
@Inject
constructor(
  private val json: Json
) {

  fun encode(message: ExecutorMessage): ByteArray {
    return json.encodeToString(ExecutorMessage.serializer(), message)
      .toByteArray(Charsets.UTF_8)
  }

  fun decode(message: ByteArray?): ExecutorMessage {
    val messageJson = message?.toString(Charsets.UTF_8)
      ?: throw IllegalArgumentException("Message body is null")
    return json.decodeFromString<ExecutorMessage>(messageJson)  }
}
