package karya.core.entities

import karya.core.entities.requests.CreateUserRequest
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

/**
 * Data class representing a user.
 *
 * @property id The unique identifier of the user.
 * @property name The name of the user.
 * @property createdAt The timestamp when the user was created.
 */
@Serializable
data class User(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  val name: String,
  val createdAt: Long,
) {
  /**
   * Secondary constructor to create a User instance from a CreateUserRequest.
   *
   * @param request The request object containing the user details.
   */
  constructor(request: CreateUserRequest) : this(
    id = UUID.randomUUID(),
    name = request.name,
    createdAt = Instant.now().toEpochMilli(),
  )
}
