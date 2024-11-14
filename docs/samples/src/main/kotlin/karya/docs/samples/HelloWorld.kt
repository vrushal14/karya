package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.Action
import karya.core.entities.enums.JobType
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest
import karya.core.entities.requests.UpdateJobRequest

suspend fun main() {
  val client = KaryaClientFactory.create(KaryaClientConfig.Dev)

  val user = client.createUser(CreateUserRequest("Alice"))
  val job = client.submitJob(
      SubmitJobRequest(
      userId = user.id,
      periodTime = "PT7S",
      jobType = JobType.RECURRING,
      action = Action.HttpInvocation("http://google.com")
    )
  )
  client.fetchJob(job.id).also { println(it) }

  client.updateJob(UpdateJobRequest(
    jobId = job.id,
    maxFailureRetry = 42,
  )).also { println(it) }

//  client.cancelJob(UUID.fromString("9cf88d16-fac7-4abd-9bb6-3ccf497f28bb")).also { println(it) }
}