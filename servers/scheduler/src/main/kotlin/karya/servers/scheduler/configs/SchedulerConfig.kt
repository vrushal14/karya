package karya.servers.scheduler.configs

import karya.core.configs.LocksConfig
import karya.core.utils.PropsReader.getProperty
import karya.data.fused.LocksSelector
import karya.data.fused.repos.RepoConfig
import java.io.File
import java.util.*

data class SchedulerConfig(
  val threadCount : Int,
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

      return SchedulerConfig(
        threadCount = getProperty(properties, "application.threadCount"),
        workers = getProperty(properties, "application.workers"),
        startDelay = getProperty(properties, "application.workers.startDelay"),

        channelCapacity = getProperty(properties, "application.fetcher.channelCapacity"),
        pollFrequency = getProperty(properties, "application.fetcher.pollFrequency"),
        partitions = getPartitions(properties),
        executionBufferInMilli = getProperty(properties, "application.fetcher.executionBuffer"),

        repoConfig = RepoConfig.fromYaml(providerFilePath),
        locksConfig = LocksSelector.get(providerFilePath)
      )
    }

    private fun getPartitions(properties: Properties) = properties.getProperty("application.fetcher.repoPartitions")
      ?.split(",")
      ?.map { it.trim().toInt() }
      ?: throw IllegalArgumentException("application.fetcher.repoPartitions is required")
  }
}
