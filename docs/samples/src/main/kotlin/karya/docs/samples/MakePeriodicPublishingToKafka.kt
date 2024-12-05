package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import java.time.Instant

suspend fun main() {
  // Configuration for the Karya client
  val config = KaryaClientConfig.Dev
  // Create a Karya client instance
  val client = KaryaClientFactory.create(config)
  // Create a new user
  val user = client.createUser(CreateUserRequest("Bob"))

  // Create a plan request with a Kafka producer action
  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Sample run",
    periodTime = "PT15S",
    planType = PlanType.Recurring(Instant.now().plusSeconds(40).toEpochMilli()),
    action = Action.KafkaProducerRequest(
      topic = "karya-test",
      message = "Published from executor",
    ),
  )

  // Submit the plan request and print the result
  client.submitPlan(planRequest).let(::println)

  // Close the client
  client.close()
}
