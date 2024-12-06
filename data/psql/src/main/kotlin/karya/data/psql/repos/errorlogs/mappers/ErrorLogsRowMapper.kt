package karya.data.psql.repos.errorlogs.mappers

import karya.core.entities.ErrorLog
import karya.core.entities.ErrorLogType
import karya.core.exceptions.ErrorLogsException.InvalidErrorLogTypeException
import karya.core.exceptions.ErrorLogsException.TaskIdNotFoundException
import karya.data.psql.repos.errorlogs.ErrorLogsTable
import org.jetbrains.exposed.sql.ResultRow
import javax.inject.Inject

class ErrorLogsRowMapper
@Inject
constructor() {

  companion object {
    private val table = ErrorLogsTable

    private const val HOOK_ERROR_TYPE = 0
    private const val EXECUTOR_ERROR_TYPE = 1
  }

  fun toErrorType(type: ErrorLogType) = when (type) {
    is ErrorLogType.HookErrorLog -> HOOK_ERROR_TYPE
    is ErrorLogType.ExecutorErrorLog -> EXECUTOR_ERROR_TYPE
  }

  fun toTaskId(type: ErrorLogType) = when (type) {
    is ErrorLogType.HookErrorLog -> null
    is ErrorLogType.ExecutorErrorLog -> type.taskId
  }

  fun fromRecord(resultRow: ResultRow) = ErrorLog(
    planId = resultRow[table.planId],
    error = resultRow[table.error],
    type = mapErrorLogType(resultRow),
    timestamp = resultRow[table.timestamp].toEpochMilli()
  )

  private fun mapErrorLogType(resultRow: ResultRow) = when (val type = resultRow[table.type]) {
    HOOK_ERROR_TYPE -> ErrorLogType.HookErrorLog
    EXECUTOR_ERROR_TYPE -> ErrorLogType.ExecutorErrorLog(resultRow[table.taskId] ?: throw TaskIdNotFoundException())

    else -> throw InvalidErrorLogTypeException(type)
  }
}
