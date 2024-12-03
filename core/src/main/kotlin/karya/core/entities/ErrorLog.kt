package karya.core.entities

import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ErrorLog(
  @Serializable(with = UUIDSerializer::class)
  val jobId: UUID,
  val error: String,
  val type : ErrorLogType,
  val timestamp: Long
)
