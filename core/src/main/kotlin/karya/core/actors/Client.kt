package karya.core.actors

import karya.core.entities.Plan
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.entities.responses.GetPlanResponse
import karya.core.entities.responses.GetSummaryResponse
import java.util.*

interface Client {
  suspend fun createUser(request: CreateUserRequest): User

  suspend fun submitPlan(request: SubmitPlanRequest): Plan

  suspend fun getPlan(planId: UUID): GetPlanResponse

  suspend fun updatePlan(request: UpdatePlanRequest): Plan

  suspend fun cancelPlan(planId: UUID): Plan

  suspend fun getSummary(planId: UUID): GetSummaryResponse

  suspend fun close()
}
