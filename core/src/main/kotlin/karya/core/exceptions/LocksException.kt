package karya.core.exceptions

import java.util.*

sealed class LocksException : KaryaException() {

  data class UnableToAcquireLockException(
    private val entityId: UUID,
    override val message: String = "Unable to acquire lock on --- $entityId | Try again later"
  ) : LocksException()
}
