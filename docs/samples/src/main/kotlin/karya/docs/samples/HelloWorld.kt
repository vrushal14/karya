package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.enums.JobType
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitJobRequest
import karya.core.entities.requests.UpdateJobRequest
import java.net.URL

suspend fun main() {
  val client = KaryaClientFactory.create(KaryaClientConfig.Dev)

  val user = client.createUser(CreateUserRequest("Alice"))
  val job = client.submitJob(
      SubmitJobRequest(
      userId = user.id,
      periodTime = "PT7S",
      jobType = JobType.RECURRING,
      executorEndpoint = URL("http://google.com")
    )
  )
  client.fetchJob(job.id).also { println(it) }

  client.updateJob(UpdateJobRequest(
    jobId = job.id,
    maxFailureRetry = 42,
    executorEndpoint = URL("http://facebook.com")
  )).also { println(it) }

//  client.cancelJob(job.id).also { println(it) }
}