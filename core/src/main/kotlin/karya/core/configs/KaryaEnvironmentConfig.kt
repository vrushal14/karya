package karya.core.configs

import karya.core.exceptions.ConfigException.EnvironmentVariableNotSetException

/**
 * Object representing the configuration for the Karya environment.
 */
object KaryaEnvironmentConfig {

  /**
   * The configuration path for providers.
   */
  val PROVIDERS by lazy { getEnv("KARYA_PROVIDERS_CONFIG_PATH") }

  /**
   * The configuration path for the server.
   */
  val SERVER by lazy { getEnv("KARYA_SERVER_CONFIG_PATH") }

  /**
   * The configuration path for the scheduler.
   */
  val SCHEDULER by lazy { getEnv("KARYA_SCHEDULER_CONFIG_PATH") }

  /**
   * The configuration path for the executor.
   */
  val EXECUTOR by lazy { getEnv("KARYA_EXECUTOR_CONFIG_PATH") }

  /**
   * Retrieves the value of the specified environment variable.
   *
   * @param name The name of the environment variable.
   * @return The value of the environment variable.
   * @throws EnvironmentVariableNotSetException if the environment variable is not set.
   */
  private fun getEnv(name: String): String =
    System.getenv(name) ?: throw EnvironmentVariableNotSetException(name)
}
