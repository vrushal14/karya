package karya.core.exceptions

sealed class ConfigException : KaryaException() {

  data class EnvironmentVariableNotSetException(
    private val variable: String,
    override val message: String = "Environment variable [$variable] not set"
  ) : ConfigException()

  data class YamlSectionNotFoundException(
    private val sectionName: String,
    override val message: String = "Section [$sectionName] not found in YAML file"
  ) : ConfigException()

  data class YamlMapKeyNotSetException(
    private val key: String,
    override val message: String = "YAML map key [$key] value is not set"
  ) : ConfigException()

  data class InvalidYamlMapKeyException(
    private val key: String,
    private val expected: String,
    override val message: String = "Invalid type for key [$key] | Expected : [$expected]"
  ) : ConfigException()
}
