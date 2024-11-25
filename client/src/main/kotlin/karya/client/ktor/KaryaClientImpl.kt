package karya.client.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import karya.client.ktor.utils.deserialize
import karya.core.actors.Client
import karya.core.entities.Job
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest
import karya.core.entities.requests.UpdateJobRequest
import karya.core.entities.responses.GetJobResponse
import karya.core.exceptions.KaryaException
import kotlinx.serialization.json.Json
import java.util.*

class KaryaClientImpl(
    private val httpClient: HttpClient,
    private val json: Json,
) : Client {
    companion object {
        private const val VERSION = "v1"
    }

    override suspend fun createUser(request: CreateUserRequest): User =
        httpClient
            .post {
                url { path(VERSION, "user") }
                setBody(request)
            }.deserialize<User, KaryaException>(json)

    override suspend fun submitJob(request: SubmitJobRequest): Job =
        httpClient
            .post {
                url { path(VERSION, "job") }
                setBody(request)
            }.deserialize<Job, KaryaException>(json)

    override suspend fun fetchJob(jobId: UUID): GetJobResponse =
        httpClient
            .get {
                url { path(VERSION, "job", jobId.toString()) }
            }.deserialize<GetJobResponse, KaryaException>(json)

    override suspend fun updateJob(request: UpdateJobRequest): Job =
        httpClient
            .patch {
                url { path(VERSION, "job") }
                setBody(request)
            }.deserialize<Job, KaryaException>(json)

    override suspend fun cancelJob(jobId: UUID): Job =
        httpClient
            .post {
                url { path(VERSION, "job", jobId.toString()) }
            }.deserialize<Job, KaryaException>(json)
}
