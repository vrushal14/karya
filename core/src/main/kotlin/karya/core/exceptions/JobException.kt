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

  data class RecursiveDepthExceededException(
    private val passed: Int,
    private val allowed: Int,
    override val message: String = "Depth exceeded for chained request --- passed: $passed | allowed: $allowed",
  ) : JobException()

  data class InvalidChainedRequestException(
    override val message: String = "Chained requests cannot be recurring",
  ) : JobException()
}
