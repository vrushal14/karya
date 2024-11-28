package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.action.Action
import karya.core.entities.enums.JobType
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest

suspend fun main() {
  val client = KaryaClientFactory.create(KaryaClientConfig.Dev)
  val user = client.createUser(CreateUserRequest("Bob"))

  val request = SubmitJobRequest(
    userId = user.id,
    description = "Sample chained run",
    periodTime = "PT10S",
    jobType = JobType.ONE_TIME,
    action = Action.ChainedRequest(
      request = SubmitJobRequest(
        userId = user.id,
        description = "Chained delay run",
        periodTime = "PT5S",
        jobType = JobType.RECURRING,
        action = Action.RestApiRequest(
          baseUrl = "eox7wbcodh9parh.m.pipedream.net",
        ),
      ),
    ),
  )
}
