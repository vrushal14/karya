package karya.servers.scheduler.configs

import karya.data.fused.locks.LocksConfig
import karya.data.fused.repos.RepoConfig
import java.io.File
import java.util.*

data class SchedulerConfig(
  val workers : Int,
  val startDelay : Long,

  val channelCapacity : Int,
  val pollFrequency : Long,
  val partitions : List<Int>,
  val executionBufferInMilli : Long,

  val repoConfig: RepoConfig,
  val locksConfig: LocksConfig
) {

  companion object {
    fun load(schedulerFilePath: String, providerFilePath: String): SchedulerConfig {
      val properties = Properties().apply {
        load(File(schedulerFilePath).inputStream())
      }

      val partitions = properties.getProperty("application.fetcher.repoPartitions")
        ?.split(",")
        ?.map { it.trim().toInt() }
        ?: throw IllegalArgumentException("application.fetcher.repoPartitions is required")

      return SchedulerConfig(
        workers = getProperty(properties, "application.workers").toInt(),
        startDelay = getProperty(properties, "application.workers.startDelay"),
        channelCapacity = getProperty(properties, "application.fetcher.channelCapacity").toInt(),
        pollFrequency = getProperty(properties, "application.fetcher.pollFrequency"),
        partitions = partitions,
        executionBufferInMilli = getProperty(properties, "application.fetcher.executionBuffer"),
        repoConfig = RepoConfig.fromYaml(providerFilePath),
        locksConfig = LocksConfig.fromYaml(providerFilePath)
      )
    }

    private fun getProperty(properties: Properties, value : String) = properties.getProperty(value)?.toLong()
      ?: throw IllegalArgumentException("$value is required")
  }
}
