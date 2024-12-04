package karya.core.exceptions

/**
 * Sealed class representing configuration-related exceptions.
 */
sealed class ConfigException : KaryaException() {

  /**
   * Exception thrown when an environment variable is not set.
   *
   * @property variable The name of the environment variable that is not set.
   * @property message The error message.
   */
  data class EnvironmentVariableNotSetException(
    private val variable: String,
    override val message: String = "Environment variable [$variable] not set"
  ) : ConfigException()

  /**
   * Exception thrown when a section is not found in a YAML file.
   *
   * @property sectionName The name of the missing section.
   * @property message The error message.
   */
  data class YamlSectionNotFoundException(
    private val sectionName: String,
    override val message: String = "Section [$sectionName] not found in YAML file"
  ) : ConfigException()

  /**
   * Exception thrown when a YAML map key value is not set.
   *
   * @property key The key whose value is not set.
   * @property message The error message.
   */
  data class YamlMapKeyNotSetException(
    private val key: String,
    override val message: String = "YAML map key [$key] value is not set"
  ) : ConfigException()

  /**
   * Exception thrown when a YAML map key has an invalid type.
   *
   * @property key The key with the invalid type.
   * @property expected The expected type for the key.
   * @property message The error message.
   */
  data class InvalidYamlMapKeyException(
    private val key: String,
    private val expected: String,
    override val message: String = "Invalid type for key [$key] | Expected : [$expected]"
  ) : ConfigException()
}
