package karya.servers.scheduler.configs

import karya.core.configs.LocksConfig
import karya.core.configs.QueueConfig
import karya.core.configs.RepoConfig
import karya.core.utils.PropsReader.getKey
import karya.data.fused.LocksSelector
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector
import java.io.File
import java.util.*

data class SchedulerConfig(
  val threadCount : Int,
  val workers : Int,

  val channelCapacity : Int,
  val pollFrequency : Long,
  val partitions : List<Int>,
  val executionBufferInMilli : Long,

  val repoConfig: RepoConfig,
  val locksConfig: LocksConfig,
  val queueConfig: QueueConfig
) {

  companion object {
    fun load(schedulerFilePath: String, providerFilePath: String): SchedulerConfig {
      val properties = Properties().apply {
        load(File(schedulerFilePath).inputStream())
      }

      return SchedulerConfig(
        threadCount = properties.getKey("application.threadCount"),
        workers = properties.getKey("application.workers"),

        channelCapacity = properties.getKey("application.fetcher.channelCapacity"),
        pollFrequency = properties.getKey("application.fetcher.pollFrequency"),
        partitions = getPartitions(properties),
        executionBufferInMilli = properties.getKey("application.fetcher.executionBuffer"),

        repoConfig = RepoSelector.get(providerFilePath),
        locksConfig = LocksSelector.get(providerFilePath),
        queueConfig = QueueSelector.get(providerFilePath)
      )
    }

    private fun getPartitions(properties: Properties) = properties.getProperty("application.fetcher.repoPartitions")
      ?.split(",")
      ?.map { it.trim().toInt() }
      ?: throw IllegalArgumentException("application.fetcher.repoPartitions is required")
  }
}
