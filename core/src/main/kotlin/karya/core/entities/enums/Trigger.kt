package karya.core.entities.enums

/**
 * Enum class representing the different triggers for hook actions.
 */
enum class Trigger {
  /**
   * Trigger for when a hook action should occur on failure.
   */
  ON_FAILURE,

  /**
   * Trigger for when a hook action should occur on completion.
   */
  ON_COMPLETION
}
