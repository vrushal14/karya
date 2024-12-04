package karya.connectors.slackmessage

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.ExecutorResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import java.time.Instant
import java.util.*
import javax.inject.Inject

class SlackMessageConnector
@Inject
constructor(
  private val methodsClient: MethodsClient,
  private val slack: Slack
) : Connector<Action.SlackMessageRequest> {

  companion object {
    private val endNote = buildJsonObject {
      put("type", JsonPrimitive("section"))
      put("text", buildJsonObject {
        put("type", JsonPrimitive("mrkdwn"))
        put("text", JsonPrimitive("_sent via Karya_"))
      })
    }
  }

  override suspend fun invoke(planId: UUID, action: Action.SlackMessageRequest): ExecutorResult {
    val request = createMessageRequest(action)
    val response = methodsClient.chatPostMessage(request)
    val timestamp = Instant.now().toEpochMilli()
    return when (response.isOk) {
      true -> ExecutorResult.Success(timestamp)
      false -> ExecutorResult.Failure(response.error, action, timestamp)
    }
  }

  override suspend fun shutdown() {
    slack.close()
  }

  private fun createMessageRequest(action: Action.SlackMessageRequest): ChatPostMessageRequest {
    val existingBlocks = Json.parseToJsonElement(action.message).jsonArray
    val updatedBlocks = existingBlocks.toMutableList()
    updatedBlocks.add(endNote)

    return ChatPostMessageRequest.builder()
      .channel(action.channel)
      .blocksAsString(updatedBlocks.toString())
      .build()
  }
}
