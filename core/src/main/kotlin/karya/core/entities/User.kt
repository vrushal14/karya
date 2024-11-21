package karya.core.entities

import karya.core.entities.requests.CreateUserRequest
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class User(
	@Serializable(with = UUIDSerializer::class)
	val id: UUID,
	val name: String,
	val createdAt: Long,
) {
	constructor(request: CreateUserRequest) : this(
		id = UUID.randomUUID(),
		name = request.name,
		createdAt = Instant.now().toEpochMilli(),
	)
}
