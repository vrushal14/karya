package karya.core.entities

import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ErrorLog(
  @Serializable(with = UUIDSerializer::class)
  val planId: UUID,
  val error: String,
  val type: ErrorLogType,
  val timestamp: Long
)
