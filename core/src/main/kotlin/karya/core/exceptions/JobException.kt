package karya.core.exceptions

import java.util.*

sealed class JobException(
  override val message: String
) : KaryaException(message) {

  data class JobNotFoundException(
    val id: UUID,
    override val message: String = "Job not found --- $id"
  ) : JobException(message)

  data class UnknownJobTypeException(
    val jobTypeId: Int,
    override val message: String = "Unknown Job Type ID --- $jobTypeId"
  ) : JobException(message)

  data class UnknownJobStatusException(
    val jobStatusId: Int,
    override val message: String = "Unknown Job Status ID --- $jobStatusId"
  ) : JobException(message)

}