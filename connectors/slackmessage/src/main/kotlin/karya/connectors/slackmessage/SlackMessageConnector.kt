package karya.connectors.slackmessage

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.ExecutorResult
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*
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
    private val listSerializer = ListSerializer(JsonElement.serializer())
    private val endNote = buildJsonObject {
      put("type", JsonPrimitive("section"))
      put("text", buildJsonObject {
        put("type", JsonPrimitive("mrkdwn"))
        put("text", JsonPrimitive("_sent via Karya_"))
      })
    }
  }

  override suspend fun invoke(planId: UUID, action: Action.SlackMessageRequest): ExecutorResult = try {
    val request = createMessageRequest(action)
    val response = methodsClient.chatPostMessage(request)
    val timestamp = Instant.now().toEpochMilli()
    when (response.isOk) {
      true -> ExecutorResult.Success(timestamp)
      false -> ExecutorResult.Failure(response.error, action, timestamp)
    }
  } catch (e: Exception) {
    ExecutorResult.Failure(e.message ?: "Unknown error", action, Instant.now().toEpochMilli())
  }

  override suspend fun shutdown() {
    slack.close()
  }

  private fun createMessageRequest(action: Action.SlackMessageRequest): ChatPostMessageRequest {
    val existingBlocks = Json.parseToJsonElement(action.message).jsonArray
    val updatedBlocks = existingBlocks.toMutableList()
    updatedBlocks.add(endNote)

    val blocksAsString = Json.encodeToString(listSerializer, updatedBlocks)
    return ChatPostMessageRequest.builder()
      .channel(action.channel)
      .blocksAsString(blocksAsString)
      .build()
  }
}
