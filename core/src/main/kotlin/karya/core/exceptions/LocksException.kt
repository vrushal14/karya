package karya.core.exceptions

import java.util.*

/**
 * Sealed class representing exceptions related to locks.
 */
sealed class LocksException : KaryaException() {

  /**
   * Exception thrown when a lock cannot be acquired for a specific entity.
   *
   * @property entityId The unique identifier of the entity for which the lock could not be acquired.
   * @property message The error message.
   */
  data class UnableToAcquireLockException(
    private val entityId: UUID,
    override val message: String = "Unable to acquire lock on --- $entityId | Try again later"
  ) : LocksException()
}
