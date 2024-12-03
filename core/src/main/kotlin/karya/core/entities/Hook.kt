package karya.core.entities

import karya.core.entities.enums.Trigger
import kotlinx.serialization.Serializable

@Serializable
data class Hook(
  val maxRetry: Int,
  val trigger: Trigger,
  val action: Action  // eat what you kill
)
