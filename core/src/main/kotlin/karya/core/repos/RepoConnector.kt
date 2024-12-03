package karya.core.repos

interface RepoConnector {
  suspend fun getPartitions(): Int

  suspend fun runMigration(): Boolean

  suspend fun createPartitions(): Boolean

  suspend fun shutdown(): Boolean
}
