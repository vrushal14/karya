package karya.core.utils

import kotlinx.serialization.json.*

/**
 * Object responsible for serializing and deserializing maps to and from JSON strings.
 */
object MapJsonStringSerde {

  /**
   * Serializes a map to a JSON string.
   *
   * @param json The JSON instance used for serialization.
   * @param data The map to be serialized.
   * @return The serialized JSON string.
   */
  fun serialize(json: Json, data: Map<String, Any?>): String {
    val jsonElement =
      buildJsonObject {
        data.forEach { (key, value) ->
          put(key, convertToJsonElement(value))
        }
      }
    return json.encodeToString(JsonObject.serializer(), jsonElement)
  }

  /**
   * Converts a value to a JSON element.
   *
   * @param value The value to be converted.
   * @return The corresponding JSON element.
   */
  private fun convertToJsonElement(value: Any?): JsonElement =
    when (value) {
      null -> JsonNull
      is String -> JsonPrimitive(value)
      is Number -> JsonPrimitive(value)
      is Boolean -> JsonPrimitive(value)
      is List<*> -> buildJsonArray {
        value.forEach { add(convertToJsonElement(it)) }
      }

      is Map<*, *> -> buildJsonObject {
        value.forEach { (k, v) -> put(k.toString(), convertToJsonElement(v)) }
      }

      else -> JsonPrimitive(value.toString())
    }
}
