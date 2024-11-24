package karya.core.actors

import karya.core.entities.action.Action

sealed class Result {
	object Success : Result()

	data class Failure(
		val reason: String,
		val action: Action,
		val exception: Exception? = null,
	) : Result()
}

interface Connector<T : Action> {
	suspend fun invoke(action: T): Result
}
