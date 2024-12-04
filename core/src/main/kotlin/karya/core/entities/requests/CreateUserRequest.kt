package karya.core.entities.requests

import kotlinx.serialization.Serializable

/**
 * Data class representing a request to create a user.
 *
 * @property name The name of the user to be created.
 */
@Serializable
data class CreateUserRequest(
  val name: String,
)
