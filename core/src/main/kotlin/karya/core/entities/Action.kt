package karya.core.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class Action {

  @Serializable
  data class HttpInvocation(
    val url: String
  ) : Action()
}