package karya.data.psql.repos

import karya.core.entities.Job
import karya.core.entities.enums.JobStatus
import karya.core.repos.JobsRepo
import karya.data.psql.tables.jobs.JobsQueries
import java.util.*
import javax.inject.Inject

class PsqlJobsRepo
@Inject
constructor(
  private val jobsQueries: JobsQueries,
) : JobsRepo {
  override suspend fun add(job: Job) {
    jobsQueries.add(job)
  }

  override suspend fun get(id: UUID): Job? = jobsQueries.get(id)

  override suspend fun update(job: Job) {
    jobsQueries.update(job)
  }

  override suspend fun updateStatus(id: UUID, status: JobStatus, ) {
    jobsQueries.updateStatus(id, status)
  }
}
