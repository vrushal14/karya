package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.JobType
import karya.core.entities.action.Action
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest
import java.time.Instant

suspend fun main() {
  val config = KaryaClientConfig.Dev
  val client = KaryaClientFactory.create(config)
  val user = client.createUser(CreateUserRequest("Bob"))

  val request = SubmitJobRequest(
    userId = user.id,
    description = "Sample chained run",
    periodTime = "PT15S",
    jobType = JobType.OneTime,
    action = Action.ChainedRequest(
      request = SubmitJobRequest(
        userId = user.id,
        description = "Chained delay run",
        periodTime = "PT5S",
        jobType = JobType.Recurring(Instant.now().plusSeconds(30).toEpochMilli()),
        action = Action.RestApiRequest(
          baseUrl = "eox7wbcodh9parh.m.pipedream.net",
        ),
      ),
    ),
  )

  client.submitJob(request).also(::println)
}
