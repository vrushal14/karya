package karya.data.psql.tables.jobs.mappers

import karya.core.entities.enums.JobStatus
import karya.core.exceptions.JobException.UnknownJobStatusException
import javax.inject.Inject

class JobStatusMapper
@Inject
constructor() {
  companion object {
    private const val CREATED = 0
    private const val RUNNING = 1
    private const val FAILURE = -1
    private const val COMPLETED = 2
    private const val CANCELLED = 3
  }

  fun toRecord(status: JobStatus) : Int = when(status) {
    JobStatus.CREATED -> CREATED
    JobStatus.RUNNING -> RUNNING
    JobStatus.FAILURE -> FAILURE
    JobStatus.COMPLETED -> COMPLETED
    JobStatus.CANCELLED -> CANCELLED
  }

  fun fromRecord(record: Int) : JobStatus = when(record) {
    CREATED -> JobStatus.CREATED
    RUNNING -> JobStatus.RUNNING
    FAILURE -> JobStatus.FAILURE
    COMPLETED -> JobStatus.COMPLETED
    CANCELLED -> JobStatus.CANCELLED

    else -> throw UnknownJobStatusException(record)
  }
}