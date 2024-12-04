package karya.core.locks.entities

/**
 * Sealed class representing the result of a lock operation.
 */
sealed class LockResult<out T> {

  /**
   * Represents a successful lock operation.
   *
   * @param T The type of the result.
   * @property result The result of the successful lock operation.
   */
  data class Success<out T>(
    val result: T,
  ) : LockResult<T>()

  /**
   * Represents a failed lock operation.
   */
  object Failure : LockResult<Nothing>()
}
