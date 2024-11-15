package karya.core.locks

import karya.core.locks.entities.LockResult
import java.util.UUID

interface LocksClient {
  suspend fun <T> withLock(id: UUID, block: suspend () -> T): LockResult<T>
  suspend fun shutdown() : Boolean
}