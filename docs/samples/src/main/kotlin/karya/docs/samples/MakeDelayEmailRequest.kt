package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest

suspend fun main() {
  // Configuration for the Karya client
  val config = KaryaClientConfig.Dev
  // Create a Karya client instance
  val client = KaryaClientFactory.create(config)
  // Create a new user
  val user = client.createUser(CreateUserRequest("Ekko"))

  val emailRequest = Action.EmailRequest(
    recipient = "recipient@gmail.com",
    subject = "Karya notification",
    message = "Hello from Karya!"
  )

  // Create a plan request with the defined API request action
  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Sample delay email run",
    periodTime = "PT5S",
    planType = PlanType.OneTime,
    action = emailRequest
  )

  // Submit the plan request and print the result
  client.submitPlan(planRequest).let(::println)
}