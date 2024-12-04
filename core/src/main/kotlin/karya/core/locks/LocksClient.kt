package karya.core.locks

import karya.core.locks.entities.LockResult
import java.util.*

/**
 * Interface representing a client for managing locks.
 */
interface LocksClient {

  /**
   * Acquires a lock for a specific entity and executes a block of code.
   *
   * @param T The type of the result.
   * @param id The unique identifier of the entity to lock.
   * @param block The block of code to execute while holding the lock.
   * @return The result of the lock operation.
   */
  suspend fun <T> withLock(id: UUID, block: suspend () -> T, ): LockResult<T>

  /**
   * Shuts down the lock client.
   *
   * @return `true` if the shutdown was successful, `false` otherwise.
   */
  suspend fun shutdown(): Boolean
}
