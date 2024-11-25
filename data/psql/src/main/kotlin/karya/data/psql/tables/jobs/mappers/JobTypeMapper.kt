package karya.data.psql.tables.jobs.mappers

import karya.core.entities.enums.JobType
import karya.core.exceptions.JobException.UnknownJobTypeException
import javax.inject.Inject

class JobTypeMapper
@Inject
constructor() {
  companion object {
    private const val RECURRING = 0
    private const val ONE_TIME = 1
  }

  fun toRecord(type: JobType): Int =
    when (type) {
      JobType.RECURRING -> RECURRING
      JobType.ONE_TIME -> ONE_TIME
    }

  fun fromRecord(record: Int): JobType =
    when (record) {
      RECURRING -> JobType.RECURRING
      ONE_TIME -> JobType.ONE_TIME

      else -> throw UnknownJobTypeException(record)
    }
}
