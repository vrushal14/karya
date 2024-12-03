package karya.servers.server.domain.usecases.external

import karya.core.entities.responses.GetSummaryResponse
import karya.core.exceptions.JobException
import karya.core.repos.ErrorLogsRepo
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import java.util.*
import javax.inject.Inject

class GetSummary
@Inject
constructor(
  private val jobsRepo: JobsRepo,
  private val tasksRepo: TasksRepo,
  private val errorLogsRepo: ErrorLogsRepo
) {

  suspend fun invoke(jobId: UUID): GetSummaryResponse {
    val job = jobsRepo.get(jobId) ?: throw JobException.JobNotFoundException(jobId)
    val tasks = tasksRepo.get(jobId).sortedBy { it.createdAt }
    val errorLogs = errorLogsRepo.get(job.id)
    return GetSummaryResponse(job, tasks, errorLogs)
  }
}
