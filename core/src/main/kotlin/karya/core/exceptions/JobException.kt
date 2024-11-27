package karya.core.exceptions

import java.util.*

sealed class JobException : KaryaException() {

  data class JobNotFoundException(
    private val id: UUID,
    override val message: String = "Job not found --- $id",
  ) : JobException()

  data class UnknownJobTypeException(
    private val jobTypeId: Int,
    override val message: String = "Unknown Job Type ID --- $jobTypeId",
  ) : JobException()

  data class UnknownJobStatusException(
    private val jobStatusId: Int,
    override val message: String = "Unknown Job Status ID --- $jobStatusId",
  ) : JobException()
}
