package karya.core.repos

import karya.core.entities.ErrorLog
import java.util.*

/**
 * Interface representing a repository for managing error logs.
 */
interface ErrorLogsRepo {

  /**
   * Pushes an error log to the repository.
   *
   * @param log The error log to be pushed.
   */
  suspend fun push(log: ErrorLog)

  /**
   * Retrieves error logs for a specific plan.
   *
   * @param planId The unique identifier of the plan.
   * @return A list of error logs associated with the specified plan.
   */
  suspend fun get(planId: UUID): List<ErrorLog>
}
