package karya.core.configs

import karya.core.exceptions.ConfigException.EnvironmentVariableNotSetException

object KaryaEnvironmentConfig {
  val PROVIDERS by lazy { getEnv("KARYA_PROVIDERS_CONFIG_PATH") }

  val SERVER by lazy { getEnv("KARYA_SERVER_CONFIG_PATH") }
  val SCHEDULER by lazy { getEnv("KARYA_SCHEDULER_CONFIG_PATH") }
  val EXECUTOR by lazy { getEnv("KARYA_EXECUTOR_CONFIG_PATH") }

  private fun getEnv(name: String): String =
    System.getenv(name) ?: throw EnvironmentVariableNotSetException(name)
}
