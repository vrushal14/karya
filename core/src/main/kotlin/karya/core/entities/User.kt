package karya.core.entities

import karya.core.entities.requests.CreateUserRequest
import java.time.Instant
import java.util.UUID

data class User(
  val id: UUID,
  val name: String,
  val createdAt: Long
) {
  constructor(request: CreateUserRequest): this(
    id = UUID.randomUUID(),
    name = request.name,
    createdAt = Instant.now().toEpochMilli()
  )
}
