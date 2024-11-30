package karya.client.ktor

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import karya.client.ktor.utils.deserialize
import karya.core.actors.Client
import karya.core.entities.Job
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest
import karya.core.entities.requests.UpdateJobRequest
import karya.core.entities.responses.GetJobResponse
import karya.core.entities.responses.GetSummaryResponse
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.Logging
import java.util.*

class KaryaClientImpl(
  private val httpClient: HttpClient,
  private val json: Json,
) : Client {

  companion object : Logging {
    private const val VERSION = "v1"
    private const val JOB = "job"
    private const val USER = "user"
  }

  override suspend fun createUser(request: CreateUserRequest): User = httpClient
    .post {
      url { path(VERSION, USER) }
      setBody(request)
    }.deserialize<User>(json)

  override suspend fun submitJob(request: SubmitJobRequest): Job = httpClient
    .post {
      url { path(VERSION, JOB) }
      setBody(request)
    }.deserialize<Job>(json)

  override suspend fun fetchJob(jobId: UUID): GetJobResponse = httpClient
    .get {
      url { path(VERSION, JOB, jobId.toString()) }
    }.deserialize<GetJobResponse>(json)

  override suspend fun updateJob(request: UpdateJobRequest): Job = httpClient
    .patch {
      url { path(VERSION, JOB) }
      setBody(request)
    }.deserialize<Job>(json)

  override suspend fun cancelJob(jobId: UUID): Job = httpClient
    .post {
      url { path(VERSION, JOB, jobId.toString()) }
    }.deserialize<Job>(json)

  override suspend fun getSummary(jobId: UUID): GetSummaryResponse = httpClient
    .get {
      url { path(VERSION, JOB, jobId.toString(), "summary") }
    }.deserialize<GetSummaryResponse>(json)

  override suspend fun close() {
    httpClient.close()
    logger.info("Closed Karya client.")
  }
}
