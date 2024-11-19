package karya.core.entities.action.http

import kotlinx.serialization.Serializable

@Serializable
sealed class Body {

  @Serializable
  object EmptyBody : Body()
}