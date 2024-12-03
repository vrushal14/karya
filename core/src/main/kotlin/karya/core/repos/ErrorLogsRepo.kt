package karya.core.repos

import karya.core.entities.ErrorLog
import java.util.UUID

interface ErrorLogsRepo {
  suspend fun push(log: ErrorLog)
  suspend fun get(planId: UUID): List<ErrorLog>
}
