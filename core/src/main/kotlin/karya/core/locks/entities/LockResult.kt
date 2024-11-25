package karya.core.locks.entities

sealed class LockResult<out T> {
  data class Success<out T>(
    val result: T,
  ) : LockResult<T>()

  object Failure : LockResult<Nothing>()
}
