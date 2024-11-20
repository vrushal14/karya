package karya.core.entities.action.http

import kotlinx.serialization.Serializable

@Serializable
sealed class Body {

  @Serializable
  data class JsonBody(val data : Map<String, String>) : Body()

  @Serializable
  object EmptyBody : Body()
}