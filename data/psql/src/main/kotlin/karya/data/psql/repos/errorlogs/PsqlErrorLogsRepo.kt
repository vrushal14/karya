package karya.data.psql.repos.errorlogs

import karya.core.entities.ErrorLog
import karya.core.repos.ErrorLogsRepo
import karya.data.psql.repos.errorlogs.mappers.ErrorLogsRowMapper
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import javax.inject.Inject

class PsqlErrorLogsRepo
@Inject
constructor(
  private val db: Database,
  private val mapper: ErrorLogsRowMapper
) : ErrorLogsRepo {

  override suspend fun push(log: ErrorLog) {
    transaction(db) {
      ErrorLogsTable.insert {
        it[planId] = log.planId
        it[error] = log.error
        it[type] = mapper.toErrorType(log.type)
        it[taskId] = mapper.toTaskId(log.type)
        it[timestamp] = Instant.ofEpochMilli(log.timestamp)
      }
    }
  }

  override suspend fun get(planId: UUID): List<ErrorLog> = transaction(db) {
    ErrorLogsTable.selectAll()
      .where { ErrorLogsTable.planId eq planId }
      .map { mapper.fromRecord(it) }
  }
}
