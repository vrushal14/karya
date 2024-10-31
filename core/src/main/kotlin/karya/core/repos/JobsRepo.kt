package karya.core.repos

import karya.core.entities.Job
import karya.core.entities.enums.JobStatus
import java.util.UUID

interface JobsRepo {
  suspend fun add(job: Job)
  suspend fun get(id : UUID) : Job?
  suspend fun update(job: Job)
  suspend fun updateStatus(id : UUID, status: JobStatus)
}