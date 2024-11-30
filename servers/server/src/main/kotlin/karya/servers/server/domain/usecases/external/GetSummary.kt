package karya.servers.server.domain.usecases.external

import karya.core.entities.responses.GetSummaryResponse
import karya.core.exceptions.JobException
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import java.util.*
import javax.inject.Inject

class GetSummary
@Inject
constructor(
  private val jobsRepo: JobsRepo,
  private val tasksRepo: TasksRepo
) {

  suspend fun invoke(jobId: UUID): GetSummaryResponse {
    val job = jobsRepo.get(jobId) ?: throw JobException.JobNotFoundException(jobId)
    val tasks = tasksRepo.get(jobId).sortedBy { it.createdAt }
    return GetSummaryResponse(job, tasks)
  }
}
