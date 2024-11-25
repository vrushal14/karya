package karya.core.entities.action.http

import karya.core.utils.MapJsonStringSerde
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed class Body {
  @Serializable
  data class JsonBody(
    val jsonString: String,
  ) : Body() {
    companion object {
      private val json =
        Json {
          ignoreUnknownKeys = true
          isLenient = true
        }
    }

    constructor(data: Map<String, Any>) : this(
      jsonString = MapJsonStringSerde.serialize(json, data),
    )
  }

  @Serializable
  object EmptyBody : Body()
}
