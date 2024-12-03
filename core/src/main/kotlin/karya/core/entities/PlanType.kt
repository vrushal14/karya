package karya.core.entities

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
sealed class PlanType {

  @Serializable
  data class Recurring(val endAt: Long?) : PlanType() {  // pass null to make it infinite
    fun isEnded(): Boolean =
      endAt?.let { Instant.now().toEpochMilli() >= it } == true
  }

  @Serializable
  object OneTime : PlanType()
}
