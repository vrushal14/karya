package karya.client.ktor

import com.fasterxml.jackson.databind.ObjectMapper
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
import karya.core.exceptions.KaryaException
import java.util.*

class KaryaClientImpl(
  private val httpClient: HttpClient,
  private val objectMapper: ObjectMapper
) : Client {

  companion object {
    private const val VERSION = "v1"
  }

  override suspend fun createUser(request: CreateUserRequest): User =
    httpClient.post {
      url { path(VERSION, "user") }
      setBody(request)
    }.deserialize<User, KaryaException>(objectMapper)

  override suspend fun submitJob(request: SubmitJobRequest): Job =
    httpClient.post {
      url { path(VERSION, "job") }
      setBody(request)
    }.deserialize<Job, KaryaException>(objectMapper)

  override suspend fun fetchJob(jobId: UUID): GetJobResponse =
    httpClient.get {
      url { path(VERSION, "job", jobId.toString()) }
    }.deserialize<GetJobResponse, KaryaException>(objectMapper)

  override suspend fun updateJob(request: UpdateJobRequest): Job =
    httpClient.patch {
      url { path(VERSION, "job") }
      setBody(request)
    }.deserialize<Job, KaryaException>(objectMapper)

  override suspend fun cancelJob(jobId: UUID): Job =
    httpClient.post {
      url { path(VERSION, "job", jobId.toString()) }
    }.deserialize<Job, KaryaException>(objectMapper)
}