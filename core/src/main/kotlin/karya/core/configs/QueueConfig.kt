package karya.core.configs

/**
 * Abstract class representing the configuration for queues.
 *
 * @property provider This helps reference what interface is being used to provide the queue.
 */
abstract class QueueConfig(
  val provider: String,
)
