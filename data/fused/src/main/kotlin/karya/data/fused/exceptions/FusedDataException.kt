package karya.data.fused.exceptions

import karya.core.exceptions.KaryaException

/**
 * Sealed class representing exceptions specific to fused data operations.
 */
sealed class FusedDataException : KaryaException() {

  /**
   * Exception thrown when an unknown provider is encountered.
   *
   * @property rootLevelEntity The root level entity for which the provider is unknown.
   * @property providerPassed The provider that was passed and is unknown.
   * @property message The exception message.
   */
  data class UnknownProviderException(
    private val rootLevelEntity: String,
    private val providerPassed: String,
    override val message: String = "Unknown provider [$providerPassed] for entity : [$rootLevelEntity]"
  ) : FusedDataException()
}
