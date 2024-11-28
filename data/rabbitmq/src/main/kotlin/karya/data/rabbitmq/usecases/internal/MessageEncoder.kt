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
}
