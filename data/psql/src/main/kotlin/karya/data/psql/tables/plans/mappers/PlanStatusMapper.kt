package karya.data.psql.tables.plans.mappers

import karya.core.entities.enums.PlanStatus
import karya.core.exceptions.PlanException.UnknownPlanStatusException
import javax.inject.Inject

class PlanStatusMapper
@Inject
constructor() {
  companion object {
    private const val CREATED = 0
    private const val RUNNING = 1
    private const val COMPLETED = 2
    private const val CANCELLED = 3
  }

  fun toRecord(status: PlanStatus): Int =
    when (status) {
      PlanStatus.CREATED -> CREATED
      PlanStatus.RUNNING -> RUNNING
      PlanStatus.COMPLETED -> COMPLETED
      PlanStatus.CANCELLED -> CANCELLED
    }

  fun fromRecord(record: Int): PlanStatus =
    when (record) {
      CREATED -> PlanStatus.CREATED
      RUNNING -> PlanStatus.RUNNING
      COMPLETED -> PlanStatus.COMPLETED
      CANCELLED -> PlanStatus.CANCELLED

      else -> throw UnknownPlanStatusException(record)
    }
}
