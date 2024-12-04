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

/**
 * Connector implementation for handling Slack message requests.
 *
 * @property methodsClient The Slack MethodsClient used for making API requests.
 * @property slack The Slack instance used for managing the connection.
 */
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

  /**
   * Invokes the Slack message request with the given plan ID and action.
   *
   * @param planId The ID of the plan.
   * @param action The action containing the Slack message request details.
   * @return The result of the execution.
   */
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

  /**
   * Shuts down the connector and releases any resources.
   */
  override suspend fun shutdown() {
    slack.close()
  }

  /**
   * Creates a ChatPostMessageRequest from the given action.
   *
   * @param action The action containing the Slack message request details.
   * @return The constructed ChatPostMessageRequest.
   */
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
