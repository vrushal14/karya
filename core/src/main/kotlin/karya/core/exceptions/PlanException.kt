package karya.core.exceptions

import java.util.*

sealed class PlanException : KaryaException() {

  data class PlanNotFoundException(
    private val id: UUID,
    override val message: String = "Plan not found --- $id",
  ) : PlanException()

  data class UnknownPlanTypeException(
    private val planTypeId: Int,
    override val message: String = "Unknown Plan Type ID --- $planTypeId",
  ) : PlanException()

  data class UnknownPlanStatusException(
    private val planStatusId: Int,
    override val message: String = "Unknown Plan Status ID --- $planStatusId",
  ) : PlanException()

  data class RecursiveDepthExceededException(
    private val passed: Int,
    private val allowed: Int,
    override val message: String = "Depth exceeded for chained request --- passed: $passed | allowed: $allowed",
  ) : PlanException()

  data class InvalidChainedRequestException(
    override val message: String = "Chained requests cannot be recurring",
  ) : PlanException()
}
