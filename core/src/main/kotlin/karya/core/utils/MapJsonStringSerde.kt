package karya.core.utils

import kotlinx.serialization.json.*

object MapJsonStringSerde {
  fun serialize(
    json: Json,
    data: Map<String, Any?>,
  ): String {
    val jsonElement =
      buildJsonObject {
        data.forEach { (key, value) ->
          put(key, convertToJsonElement(value))
        }
      }
    return json.encodeToString(JsonObject.serializer(), jsonElement)
  }

  private fun convertToJsonElement(value: Any?): JsonElement =
    when (value) {
      null -> JsonNull
      is String -> JsonPrimitive(value)
      is Number -> JsonPrimitive(value)
      is Boolean -> JsonPrimitive(value)
      is List<*> ->
        buildJsonArray {
          value.forEach {
            add(convertToJsonElement(it))
          }
        }

      is Map<*, *> ->
        buildJsonObject {
          (value as Map<String, *>).forEach { (k, v) ->
            put(k, convertToJsonElement(v))
          }
        }

      else -> JsonPrimitive(value.toString())
    }
}
