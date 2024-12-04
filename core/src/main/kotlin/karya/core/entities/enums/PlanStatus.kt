package karya.core.entities.enums

/**
 * Enum class representing the status of a plan.
 */
enum class PlanStatus {
  /**
   * Status indicating that the plan has been created.
   */
  CREATED,

  /**
   * Status indicating that the plan is currently running.
   */
  RUNNING,

  /**
   * Status indicating that the plan has been completed.
   */
  COMPLETED,

  /**
   * Status indicating that the plan has been cancelled.
   */
  CANCELLED,
}
