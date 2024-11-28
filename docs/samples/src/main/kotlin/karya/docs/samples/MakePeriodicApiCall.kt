package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.action.Action
import karya.core.entities.action.http.Body
import karya.core.entities.action.http.Method
import karya.core.entities.action.http.Protocol
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
      description = "Sample run",
      periodTime = "PT7S",
      jobType = JobType.RECURRING,
      action =
        Action.RestApiRequest(
          protocol = Protocol.HTTPS,
          baseUrl = "eox7wbcodh9parh.m.pipedream.net",
          method = Method.POST,
          headers =
            mapOf(
              "content-type" to "application/json",
              "client-header" to "Alice",
            ),
          body =
            Body.JsonBody(
              data =
                mapOf(
                  "udf1" to "value",
                  "udf2" to 1,
                  "udf3" to true,
                  "udf4" to 1.2,
                  "udf5" to listOf(1, 2, 3, 4),
                  "udf6" to
                      mapOf(
                        "nested-udf1" to listOf("a", "b", "c"),
                        "nested-udf2" to mapOf("nested-nested-udf1" to true),
                      ),
                  "udf7" to
                      listOf(
                        mapOf("nested-udf3" to listOf(1, 2, 3)),
                      ),
                ),
            ),
          timeout = 1000L,
        ),
    ),
  )
  client.fetchJob(job.id).also(::println)

  client
    .updateJob(
      UpdateJobRequest(
        jobId = job.id,
        maxFailureRetry = 4,
      ),
    ).also (::println)

//  client.cancelJob(UUID.fromString("b92a1108-a16d-4b6c-b33a-e7ebf284b613")).also(::println)
}
