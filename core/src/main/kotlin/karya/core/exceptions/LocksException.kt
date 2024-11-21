package karya.core.exceptions

import java.util.UUID

sealed class LocksException(
	override val message: String,
) : KaryaException(message) {
	data class UnableToAcquireLockException(
		val entityId: UUID,
	) : LocksException("Unable to acquire lock on --- $entityId | Try again later")
}
