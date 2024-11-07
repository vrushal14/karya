package karya.servers.scheduler.configs

import java.io.File
import java.util.*

data class SchedulerConfig(
  val pollFrequency : Long,
  val partitions : List<Int>,
  val executionBufferInMilli : Long
) {

  companion object {
    fun load(filePath: String): SchedulerConfig {
      val properties = Properties().apply {
        load(File(filePath).inputStream())
      }

      val pollFrequency = properties.getProperty("pollFrequencyInMillis")?.toLong()
        ?: throw IllegalArgumentException("pollFrequencyInMillis is required")

      val executionBuffer = properties.getProperty("executionBufferInMillis")?.toLong()
        ?: throw IllegalArgumentException("executionBufferInMillis is required")

      val partitions = properties.getProperty("repoPartitionsToPoll")
        ?.split(",")
        ?.map { it.trim().toInt() }
        ?: throw IllegalArgumentException("repoPartitionsToPoll is required")

      return SchedulerConfig(
        pollFrequency = pollFrequency,
        partitions = partitions,
        executionBufferInMilli = executionBuffer
      )
    }
  }

  fun getName() =
    "scheduler-karya-${this.hashCode()}"
}
