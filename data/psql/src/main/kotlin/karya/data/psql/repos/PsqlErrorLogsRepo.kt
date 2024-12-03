package karya.data.psql.repos

import karya.core.entities.ErrorLog
import karya.core.repos.ErrorLogsRepo
import karya.data.psql.tables.errorlogs.ErrorLogsQueries
import java.util.*
import javax.inject.Inject

class PsqlErrorLogsRepo
@Inject
constructor(
  private val errorLogsQueries: ErrorLogsQueries
) : ErrorLogsRepo {

  override suspend fun push(log: ErrorLog) {
    errorLogsQueries.add(log)
  }

  override suspend fun get(planId: UUID): List<ErrorLog> =
    errorLogsQueries.getByPlanId(planId)
}
