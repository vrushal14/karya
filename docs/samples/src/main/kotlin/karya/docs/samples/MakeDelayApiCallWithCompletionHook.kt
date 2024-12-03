package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.Action
import karya.core.entities.Hook
import karya.core.entities.PlanType
import karya.core.entities.enums.Trigger
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import java.time.Instant

suspend fun main() {
  val client = KaryaClientFactory.create(KaryaClientConfig.Dev)
  val user = client.createUser(CreateUserRequest("Jinx"))

  val completionHook = Hook(
    trigger = Trigger.ON_COMPLETION,
    action = Action.RestApiRequest(baseUrl = "http://localhost:35423"),
    maxRetry = 1
  )
  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Delay API call with completion hook",
    periodTime = "PT15S",
    planType = PlanType.Recurring(Instant.now().plusSeconds(40).toEpochMilli()),
    action = Action.RestApiRequest(
      baseUrl = "eox7wbcodh9parh.m.pipedream.net",
    ),
    hooks = listOf(
      completionHook
    )
  )

  client.submitPlan(planRequest).also { plan -> println(plan) }

  client.close()
}
