package karya.servers.server.domain.usecases

import karya.core.entities.responses.GetJobResponse
import karya.core.exceptions.JobException.JobNotFoundException
import karya.core.exceptions.TaskException.TaskNotCreatedException
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import java.util.UUID
import javax.inject.Inject

class GetJob
@Inject
constructor(
  private val jobsRepo: JobsRepo,
  private val tasksRepo: TasksRepo
){

  suspend fun invoke(jobId : UUID) : GetJobResponse {
    val job = jobsRepo.get(jobId) ?: throw JobNotFoundException(jobId)
    val latestTask = tasksRepo.getLatest(jobId) ?: throw TaskNotCreatedException(jobId)

    return GetJobResponse(job, latestTask)
  }
}