package karya.core.locks

import java.util.UUID

interface LocksClient {
  suspend fun getLock(id : UUID) : Boolean
  suspend fun freeLock(id: UUID)
  suspend fun <T> withLock(id: UUID, block: suspend () -> T): Boolean
  suspend fun shutdown() : Boolean
}