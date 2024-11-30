package karya.core.entities

import karya.core.utils.UUIDSerializer
import karya.core.entities.http.Protocol
import karya.core.entities.enums.NotificationTrigger
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class NotificationSettings {

  @Serializable
  object None : NotificationSettings()

  @Serializable
  data class Webhook(
    val trigger: NotificationTrigger,
    val protocol: Protocol,
    val baseUrl: String,
    val headers: Map<String, String> = mapOf()
  ) : NotificationSettings() {

    companion object {

      @Serializable
      sealed class Response {

        @Serializable
        data class Success(
          @Serializable(with = UUIDSerializer::class)
          val jobId: UUID
        ) : Response()

        @Serializable
        data class Failure(
          @Serializable(with = UUIDSerializer::class)
          val jobId: UUID,
          val errorMessage: String
        ) : Response()
      }
    }
  }
}
