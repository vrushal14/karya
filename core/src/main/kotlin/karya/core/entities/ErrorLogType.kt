package karya.core.entities

import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class ErrorLogType {

  @Serializable
  object HookErrorLog : ErrorLogType()

  @Serializable
  data class ExecutorErrorLog(
    @Serializable(with = UUIDSerializer::class)
    val taskId: UUID
  ) : ErrorLogType()
}
