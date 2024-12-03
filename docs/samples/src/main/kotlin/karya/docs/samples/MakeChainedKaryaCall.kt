package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.PlanType
import karya.core.entities.Action
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import java.time.Instant

suspend fun main() {
  val config = KaryaClientConfig.Dev
  val client = KaryaClientFactory.create(config)
  val user = client.createUser(CreateUserRequest("Bob"))

  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Sample chained run",
    periodTime = "PT15S",
    planType = PlanType.OneTime,
    action = Action.ChainedRequest(
      request = SubmitPlanRequest(
        userId = user.id,
        description = "Chained delay run",
        periodTime = "PT5S",
        planType = PlanType.Recurring(Instant.now().plusSeconds(30).toEpochMilli()),
        action = Action.RestApiRequest(
          baseUrl = "eox7wbcodh9parh.m.pipedream.net",
        ),
      ),
    ),
  )

  client.submitPlan(planRequest).also(::println)

//  client.cancelTask(UUID.fromString("c573d5c7-0a99-4b39-bf43-500cbc549a15"))

  client.close()
}
