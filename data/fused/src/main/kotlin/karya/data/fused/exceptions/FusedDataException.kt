package karya.data.fused.exceptions

import karya.core.exceptions.KaryaException

sealed class FusedDataException : KaryaException() {

  data class UnknownProviderException(
    private val rootLevelEntity: String,
    private val providerPassed: String,
    override val message: String = "Unknown provider [$providerPassed] for entity : [$rootLevelEntity]"
  ) : FusedDataException()
}
