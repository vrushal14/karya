package karya.core.exceptions

import java.util.*

/**
 * Sealed class representing exceptions related to plans.
 */
sealed class PlanException : KaryaException() {

  /**
   * Exception thrown when a plan is not found.
   *
   * @property id The unique identifier of the plan that was not found.
   * @property message The error message.
   */
  data class PlanNotFoundException(
    private val id: UUID,
    override val message: String = "Plan not found --- $id",
  ) : PlanException()

  /**
   * Exception thrown when an unknown plan type is encountered.
   *
   * @property planTypeId The identifier of the unknown plan type.
   * @property message The error message.
   */
  data class UnknownPlanTypeException(
    private val planTypeId: Int,
    override val message: String = "Unknown Plan Type ID --- $planTypeId",
  ) : PlanException()

  /**
   * Exception thrown when an unknown plan status is encountered.
   *
   * @property planStatusId The identifier of the unknown plan status.
   * @property message The error message.
   */
  data class UnknownPlanStatusException(
    private val planStatusId: Int,
    override val message: String = "Unknown Plan Status ID --- $planStatusId",
  ) : PlanException()

  /**
   * Exception thrown when the recursive depth for a chained request exceeds the allowed limit.
   *
   * @property passed The depth that was passed.
   * @property allowed The allowed depth.
   * @property message The error message.
   */
  data class RecursiveDepthExceededException(
    private val passed: Int,
    private val allowed: Int,
    override val message: String = "Depth exceeded for chained request --- passed: $passed | allowed: $allowed",
  ) : PlanException()

  /**
   * Exception thrown when a chained request is invalid.
   *
   * @property message The error message.
   */
  data class InvalidChainedRequestException(
    override val message: String = "Chained requests cannot be recurring",
  ) : PlanException()
}
