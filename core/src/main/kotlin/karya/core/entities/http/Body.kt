package karya.core.entities.http

import karya.core.utils.MapJsonStringSerde
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Sealed class representing the body of an HTTP request.
 */
@Serializable
sealed class Body {

  /**
   * Data class representing a JSON body for an HTTP request.
   *
   * @property jsonString The JSON string representing the body.
   */
  @Serializable
  data class JsonBody(
    val jsonString: String,
  ) : Body() {
    companion object {
      private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
      }
    }

    /**
     * Secondary constructor to create a JsonBody from a map.
     *
     * @param data The map to be serialized into a JSON string.
     */
    constructor(data: Map<String, Any>) : this(
      jsonString = MapJsonStringSerde.serialize(json, data),
    )
  }

  /**
   * Object representing an empty body for an HTTP request.
   */
  @Serializable
  object EmptyBody : Body()
}
