package karya.core.actors

import karya.core.entities.Job
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest
import karya.core.entities.requests.UpdateJobRequest
import karya.core.entities.responses.GetJobResponse
import karya.core.entities.responses.GetSummaryResponse
import java.util.*

interface Client {
  suspend fun createUser(request: CreateUserRequest): User

  suspend fun submitJob(request: SubmitJobRequest): Job

  suspend fun getJob(jobId: UUID): GetJobResponse

  suspend fun updateJob(request: UpdateJobRequest): Job

  suspend fun cancelJob(jobId: UUID): Job

  suspend fun getSummary(jobId: UUID): GetSummaryResponse

  suspend fun close()
}
