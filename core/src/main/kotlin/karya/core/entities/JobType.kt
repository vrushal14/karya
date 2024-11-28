package karya.core.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class JobType {

  @Serializable
  data class Recurring(val endAt: Long?) : JobType()

  @Serializable
  object OneTime : JobType()
}
