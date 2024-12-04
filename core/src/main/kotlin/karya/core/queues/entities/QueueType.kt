package karya.core.queues.entities

/**
 * Enum class representing different types of queues.
 */
enum class QueueType {
  /**
   * Queue type for executor messages.
   */
  EXECUTOR,

  /**
   * Queue type for dead letter messages.
   */
  DEAD_LETTER,

  /**
   * Queue type for hook messages.
   */
  HOOK
}
