package karya.servers.scheduler.configs

import karya.core.configs.LocksConfig
import karya.core.configs.QueueConfig
import karya.core.configs.RepoConfig
import karya.core.utils.getSection
import karya.core.utils.readValue
import karya.data.fused.LocksSelector
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector

data class SchedulerConfig(
  val threadCount: Int,
  val workers: Int,
  val channelCapacity: Int,
  val pollFrequency: Long,
  val partitions: List<Int>,
  val executionBufferInMilli: Long,
  val repoConfig: RepoConfig,
  val locksConfig: LocksConfig,
  val queueConfig: QueueConfig,
) {

  companion object {
    fun load(
      schedulerFilePath: String,
      providerFilePath: String,
    ): SchedulerConfig {

      val application: Map<String, *> = getSection(schedulerFilePath, "application")
      val fetcher = application["fetcher"] as Map<*, *>?
        ?: throw IllegalArgumentException("application.fetcher is required")

      return SchedulerConfig(
        threadCount = application.readValue("threadCount"),
        workers = application.readValue("workers"),

        channelCapacity = fetcher.readValue("channelCapacity"),
        pollFrequency = fetcher.readValue("pollFrequency"),
        executionBufferInMilli = fetcher.readValue("executionBuffer"),
        partitions = getPartitions(fetcher),

        repoConfig = RepoSelector.get(providerFilePath),
        locksConfig = LocksSelector.get(providerFilePath),
        queueConfig = QueueSelector.get(providerFilePath),
      )
    }

    private fun getPartitions(fetcherProperties: Map<*, *>) =
      fetcherProperties["repoPartitions"]?.let { it as List<*> }?.map { it as Int }
        ?: throw IllegalArgumentException("repoPartitions is required")
  }
}
