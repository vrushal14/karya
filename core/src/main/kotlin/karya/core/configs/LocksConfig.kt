package karya.core.configs

/**
 * Abstract class representing the configuration for locks.
 *
 * @property provider This helps reference what interface is being used to provide the locks.
 */
abstract class LocksConfig(
  val provider: String,
)
