package karya.core.repos.entities

import karya.core.entities.enums.TaskStatus
import java.time.Duration
import java.time.Instant

/**
 * Data class representing a request to get tasks.
 *
 * @property partitionKeys The list of partition keys to scan for while fetching tasks.
 * @property executionTime The execution before which a task should be fetched.
 * @property buffer The buffer duration to consider while fetching tasks.
 * @property status The status to filter tasks.
 */
data class GetTasksRequest(
  val partitionKeys: List<Int>,
  val executionTime: Instant,
  val buffer: Duration,
  val status: TaskStatus,
)
