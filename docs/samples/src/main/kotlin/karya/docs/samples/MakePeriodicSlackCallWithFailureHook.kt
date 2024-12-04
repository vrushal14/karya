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
  val config = KaryaClientConfig.Dev
  val client = KaryaClientFactory.create(config)
  val user = client.createUser(CreateUserRequest("Vi"))

  val failureHook = Hook(
    trigger = Trigger.ON_FAILURE,
    action = Action.RestApiRequest(
      baseUrl = "eox7wbcodh9parh.m.pipedream.net",
    ),
  )

  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Sample periodic run",
    periodTime = "PT15S",
    planType = PlanType.Recurring(Instant.now().plusSeconds(30).toEpochMilli()),
    action = Action.SlackMessageRequest(
      channel = "dummy-channel",
      message = "Hello from Karya!",
    ),
    hooks = listOf(failureHook),
  )

  client.submitPlan(planRequest).also(::println)
}
