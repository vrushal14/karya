package karya.data.psql.tables.errorlogs

import karya.core.entities.ErrorLog
import karya.data.psql.tables.errorlogs.mappers.ErrorLogsRowMapper
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import javax.inject.Inject

class ErrorLogsQueries
@Inject
constructor(
  private val db: Database,
  private val mapper: ErrorLogsRowMapper
) {

  fun add(errorLog: ErrorLog) = transaction(db) {
    ErrorLogsTable.insert {
      it[planId] = errorLog.planId
      it[error] = errorLog.error
      it[type] = mapper.toErrorType(errorLog.type)
      it[taskId] = mapper.toTaskId(errorLog.type)
      it[timestamp] = Instant.ofEpochMilli(errorLog.timestamp)
    }
  }

  fun getByPlanId(planId: UUID) = transaction(db) {
    ErrorLogsTable.selectAll()
      .where { ErrorLogsTable.planId eq planId }
      .map { mapper.fromRecord(it) }
  }
}
