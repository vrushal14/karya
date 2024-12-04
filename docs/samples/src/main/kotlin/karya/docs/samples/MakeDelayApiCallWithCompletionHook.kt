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

/**
 * Main function to demonstrate making a delay API call with a completion hook.
 */
suspend fun main() {
  // Create a Karya client instance using the development configuration
  val client = KaryaClientFactory.create(KaryaClientConfig.Dev)
  // Create a new user with the name "Jinx"
  val user = client.createUser(CreateUserRequest("Jinx"))

  // Define a completion hook that triggers on completion and makes a REST API request
  val completionHook = Hook(
    trigger = Trigger.ON_COMPLETION,
    action = Action.RestApiRequest(baseUrl = "http://localhost:35423"),
    maxRetry = 1
  )

  // Create a plan request with a delay API call and the defined completion hook
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

  // Submit the plan request and print the result
  client.submitPlan(planRequest).also { plan -> println(plan) }

  // Close the client
  client.close()
}
