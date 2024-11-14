package karya.data.psql.tables.tasks.mappers

import karya.core.entities.enums.TaskStatus
import karya.core.exceptions.TaskException.UnknownTaskStatusException
import javax.inject.Inject

class TaskStatusMapper
@Inject
constructor(){

  companion object {
    private const val CREATED = 0
    private const val PROCESSING = 1
    private const val SUCCESS = 2
    private const val FAILURE = -1
    private const val CANCELLED = 3
  }

  fun toRecord(status: TaskStatus) : Int = when(status) {
    TaskStatus.CREATED -> CREATED
    TaskStatus.PROCESSING -> PROCESSING
    TaskStatus.SUCCESS -> SUCCESS
    TaskStatus.FAILURE -> FAILURE
    TaskStatus.CANCELLED -> CANCELLED
  }

  fun fromRecord(record: Int) : TaskStatus = when(record) {
    CREATED -> TaskStatus.CREATED
    PROCESSING -> TaskStatus.PROCESSING
    SUCCESS -> TaskStatus.SUCCESS
    FAILURE -> TaskStatus.FAILURE
    CANCELLED -> TaskStatus.CANCELLED

    else -> throw UnknownTaskStatusException(record)
  }
}