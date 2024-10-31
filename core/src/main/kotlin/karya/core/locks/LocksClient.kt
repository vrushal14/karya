package karya.core.locks

import java.util.UUID

interface LocksClient {
  suspend fun getLock(id : UUID) : Boolean
  suspend fun freeLock(id: UUID)
}