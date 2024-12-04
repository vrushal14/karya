package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import java.time.Instant

/**
 * Main function to demonstrate making a chained Karya call.
 */
suspend fun main() {
  // Configuration for the Karya client
  val config = KaryaClientConfig.Dev
  // Create a Karya client instance
  val client = KaryaClientFactory.create(config)
  // Create a new user
  val user = client.createUser(CreateUserRequest("Bob"))

  // Create a plan request with a chained action
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

  // Submit the plan request and print the result
  client.submitPlan(planRequest).also(::println)
  // Optionally cancel a task by its UUID
  // client.cancelTask(UUID.fromString("c573d5c7-0a99-4b39-bf43-500cbc549a15"))

  // Close the client
  client.close()
}
