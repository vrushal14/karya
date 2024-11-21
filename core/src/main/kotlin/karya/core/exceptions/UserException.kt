package karya.core.exceptions

import java.util.UUID

sealed class UserException(
	override val message: String,
) : KaryaException(message) {
	data class UserNotFoundException(
		val userId: UUID,
		override val message: String = "User ($userId) not found",
	) : UserException(message)
}
