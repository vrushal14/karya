package karya.core.entities.enums

/**
 * Enum class representing the status of a task.
 */
enum class TaskStatus {
  /**
   * Status indicating that the task has been created.
   */
  CREATED,

  /**
   * Status indicating that the task is currently being processed.
   */
  PROCESSING,

  /**
   * Status indicating that the task has been successfully completed.
   */
  SUCCESS,

  /**
   * Status indicating that the task has failed.
   */
  FAILURE,

  /**
   * Status indicating that the task has been cancelled.
   */
  CANCELLED,
}
