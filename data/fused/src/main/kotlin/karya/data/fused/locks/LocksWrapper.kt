package karya.data.fused.locks

import karya.core.connectors.LockConnector
import karya.core.locks.LocksClient

data class LocksWrapper(
  val locksClient: LocksClient,

  val locksConnector: LockConnector
)