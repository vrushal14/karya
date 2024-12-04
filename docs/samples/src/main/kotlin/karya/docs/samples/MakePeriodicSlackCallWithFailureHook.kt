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
 * Main function to demonstrate making a periodic Slack call with a failure hook.
 */
suspend fun main() {
  // Create a Karya client instance using the development configuration
  val config = KaryaClientConfig.Dev
  val client = KaryaClientFactory.create(config)
  // Create a new user with the name "Vi"
  val user = client.createUser(CreateUserRequest("Vi"))

  // Define a failure hook that triggers on failure and makes a REST API request
  val failureHook = Hook(
    trigger = Trigger.ON_FAILURE,
    action = Action.RestApiRequest(
      baseUrl = "eox7wbcodh9parh.m.pipedream.net",
    ),
  )

  // Define the Slack message to be sent periodically
  val slackMessage = """[
    {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": "Hello, this is periodic slack message from Karya!"
        }
    }
]"""

  // Create a plan request with the defined Slack message action and failure hook
  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Sample periodic slack run",
    periodTime = "PT10S",
    planType = PlanType.Recurring(Instant.now().plusSeconds(30).toEpochMilli()),
    action = Action.SlackMessageRequest(
      channel = "C083L324V99",
      message = slackMessage,
    ),
    hooks = listOf(failureHook),
  )

  // Submit the plan request and print the result
  client.submitPlan(planRequest).also(::println)
}
