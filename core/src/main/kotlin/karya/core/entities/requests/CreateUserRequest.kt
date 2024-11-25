package karya.core.entities.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
  val name: String,
)
