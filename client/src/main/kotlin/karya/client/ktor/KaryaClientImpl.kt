package karya.client.ktor

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import karya.client.ktor.usecase.ValidateRequest
import karya.client.ktor.utils.deserialize
import karya.core.actors.Client
import karya.core.entities.Job
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest
import karya.core.entities.requests.UpdateJobRequest
import karya.core.entities.responses.GetJobResponse
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

class KaryaClientImpl
@Inject
constructor(
  private val httpClient: HttpClient,
  private val json: Json,

  private val validateRequest: ValidateRequest
) : Client {
  companion object {
    private const val VERSION = "v1"
  }

  override suspend fun createUser(request: CreateUserRequest): User =
    httpClient
      .post {
        url { path(VERSION, "user") }
        setBody(request)
      }.deserialize<User>(json)

  override suspend fun submitJob(request: SubmitJobRequest): Job {
    validateRequest.invoke(request)
    return httpClient
      .post {
        url { path(VERSION, "job") }
        setBody(request)
      }.deserialize<Job>(json)
  }

  override suspend fun fetchJob(jobId: UUID): GetJobResponse =
    httpClient
      .get {
        url { path(VERSION, "job", jobId.toString()) }
      }.deserialize<GetJobResponse>(json)

  override suspend fun updateJob(request: UpdateJobRequest): Job =
    httpClient
      .patch {
        url { path(VERSION, "job") }
        setBody(request)
      }.deserialize<Job>(json)

  override suspend fun cancelJob(jobId: UUID): Job =
    httpClient
      .post {
        url { path(VERSION, "job", jobId.toString()) }
      }.deserialize<Job>(json)

  override suspend fun close() {
    httpClient.close()
  }
}
