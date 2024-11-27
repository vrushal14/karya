package karya.core.exceptions

import java.util.*

sealed class UserException : KaryaException() {

  data class UserNotFoundException(
    private val userId: UUID,
    override val message: String = "User ($userId) not found",
  ) : UserException()
}
