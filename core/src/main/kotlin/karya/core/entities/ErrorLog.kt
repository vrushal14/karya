package karya.core.entities

import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Data class representing an error log.
 *
 * @property planId The unique identifier of the plan associated with the error.
 * @property error The error message.
 * @property type The type of the error log.
 * @property timestamp The timestamp when the error occurred.
 */
@Serializable
data class ErrorLog(
  @Serializable(with = UUIDSerializer::class)
  val planId: UUID,
  val error: String,
  val type: ErrorLogType,
  val timestamp: Long
)
