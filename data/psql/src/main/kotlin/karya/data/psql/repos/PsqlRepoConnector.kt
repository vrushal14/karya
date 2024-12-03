package karya.data.psql.repos

import com.zaxxer.hikari.HikariDataSource
import karya.core.repos.RepoConnector
import karya.data.psql.configs.PsqlRepoConfig
import org.apache.logging.log4j.kotlin.Logging
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject

class PsqlRepoConnector
@Inject
constructor(
  private val dataSource: HikariDataSource,
  private val db: Database,
  private val flyway: Flyway,
  private val config: PsqlRepoConfig,
) : RepoConnector {

  companion object : Logging

  override suspend fun getPartitions(): Int = config.partitions

  override suspend fun runMigration(): Boolean {
    try {
      flyway.migrate()
      return true
    } catch (e: Exception) {
      logger.error(e)
      return false
    }
  }

  override suspend fun createPartitions(): Boolean {
    try {
      transaction(db) {
        val partitions = config.partitions
        for (i in 1..partitions) {
          exec(
            """
							CREATE TABLE IF NOT EXISTS tasks_part_$i 
							PARTITION OF tasks 
							FOR VALUES WITH (
							    MODULUS $partitions, 
							    REMAINDER ${i % partitions}
							);
							""",
          )
        }
      }
      return true
    } catch (e: Exception) {
      logger.error(e)
      return false
    }
  }

  override suspend fun shutdown(): Boolean {
    try {
      dataSource.close()
      logger.info("PsqlRepoConnector successfully shutdown")
      return true
    } catch (e: Exception) {
      logger.error(e) { "Error shutting down PsqlRepoConnector" }
      return false
    }
  }
}
