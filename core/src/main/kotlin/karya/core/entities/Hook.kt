package karya.core.entities

import karya.core.entities.enums.Trigger
import kotlinx.serialization.Serializable

/**
 * Data class representing a hook that triggers an action.
 *
 * @property maxRetry The maximum number of retries for the hook action (default is 3).
 * @property trigger The trigger condition for the hook action.
 * @property action The action to be executed when the trigger condition is met.
 */
@Serializable
data class Hook(
  val maxRetry: Int = 3,
  val trigger: Trigger,
  val action: Action
)
