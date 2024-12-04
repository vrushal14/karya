package karya.core.repos

/**
 * Interface representing a connector for repository operations.
 */
interface RepoConnector {

  /**
   * Retrieves the number of partitions.
   *
   * @return The number of partitions.
   */
  suspend fun getPartitions(): Int

  /**
   * Runs the migration process.
   *
   * @return `true` if the migration was successful, `false` otherwise.
   */
  suspend fun runMigration(): Boolean

  /**
   * Creates partitions in the repository.
   *
   * @return `true` if the partitions were successfully created, `false` otherwise.
   */
  suspend fun createPartitions(): Boolean

  /**
   * Shuts down the repository connector.
   *
   * @return `true` if the shutdown was successful, `false` otherwise.
   */
  suspend fun shutdown(): Boolean
}
