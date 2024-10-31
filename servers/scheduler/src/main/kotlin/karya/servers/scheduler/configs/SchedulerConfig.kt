package karya.servers.scheduler.configs

data class SchedulerConfig(
  val pollFrequency : Long,
  val partitions : List<Int>,
  val executionBufferInMilli : Long
) {
  fun getName() =
    "scheduler-karya-${this.hashCode()}"
}
