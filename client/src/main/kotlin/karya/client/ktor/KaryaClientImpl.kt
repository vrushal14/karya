package karya.client.ktor

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import karya.client.ktor.utils.deserialize
import karya.core.actors.Client
import karya.core.entities.Plan
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.entities.responses.GetPlanResponse
import karya.core.entities.responses.GetSummaryResponse
import kotlinx.serialization.json.Json
import java.util.*

class KaryaClientImpl(
  private val httpClient: HttpClient,
  private val json: Json,
) : Client {

  companion object {
    private const val VERSION = "v1"
    private const val PLAN = "plan"
    private const val USER = "user"
  }

  override suspend fun createUser(request: CreateUserRequest): User = httpClient
    .post {
      url { path(VERSION, USER) }
      setBody(request)
    }.deserialize<User>(json)

  override suspend fun submitPlan(request: SubmitPlanRequest): Plan = httpClient
    .post {
      url { path(VERSION, PLAN) }
      setBody(request)
    }.deserialize<Plan>(json)

  override suspend fun getPlan(planId: UUID): GetPlanResponse = httpClient
    .get {
      url { path(VERSION, PLAN, planId.toString()) }
    }.deserialize<GetPlanResponse>(json)

  override suspend fun updatePlan(request: UpdatePlanRequest): Plan = httpClient
    .patch {
      url { path(VERSION, PLAN) }
      setBody(request)
    }.deserialize<Plan>(json)

  override suspend fun cancelPlan(planId: UUID): Plan = httpClient
    .post {
      url { path(VERSION, PLAN, planId.toString()) }
    }.deserialize<Plan>(json)

  override suspend fun getSummary(planId: UUID): GetSummaryResponse = httpClient
    .get {
      url { path(VERSION, PLAN, planId.toString(), "summary") }
    }.deserialize<GetSummaryResponse>(json)

  override suspend fun close() {
    httpClient.close()
  }
}
