package karya.servers.scheduler.usecases.internal

import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.JobType
import karya.core.entities.enums.TaskStatus
import karya.core.exceptions.JobException
import karya.core.locks.LocksClient
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.core.repos.entities.GetTasksRequest
import karya.servers.scheduler.configs.SchedulerConfig
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class ProcessTask
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val jobsRepo: JobsRepo,
  private val locksClient: LocksClient,
  private val config: SchedulerConfig,

  private val manageTasks: ManageTasks
){

  companion object : Logging

  suspend fun invoke() : Boolean {
    val task = getOpenTask() ?: return false

    if (locksClient.getLock(task.id)) {
      logger.info("[PROCESSING TASK] --- TaskId : ${task.id}")
      val job = jobsRepo.get(task.jobId) ?: throw JobException.JobNotFoundException(task.jobId)
      updateTaskStatus(job, task)

      val updatedJob = transitionJobStatus(job)
      if(shouldCreateNextTask(job)) manageTasks.invoke(updatedJob, task)
      return true
    }
    logger.warn("Unable to acquire lock on task ID --- ${task.id}")
    return false
  }

  private suspend fun getOpenTask() = GetTasksRequest(
    partitionKeys = config.partitions,
    executionTime = Instant.now(),
    buffer = Duration.ofMillis(config.executionBufferInMilli),
    status = TaskStatus.CREATED
  ).let { tasksRepo.get(it) }

  private suspend fun transitionJobStatus(job: Job) = when(job.status) {
    JobStatus.CREATED -> job.copy(status = JobStatus.RUNNING)
      .also { jobsRepo.updateStatus(job.id, JobStatus.RUNNING) }
      .also { logger.info("Transitioning job status from : ${job.status} -> ${it.status}") }
    else -> job
  }

  private suspend fun updateTaskStatus(job: Job, task: Task) = when(job.status) {
    JobStatus.CREATED, JobStatus.RUNNING -> TaskStatus.PROCESSING
    JobStatus.COMPLETED -> TaskStatus.SUCCESS
    JobStatus.FAILURE -> TaskStatus.FAILURE
    JobStatus.CANCELLED -> TaskStatus.CANCELLED
  }.also { tasksRepo.updateStatus(task.id, it) }

  private fun shouldCreateNextTask(job: Job) : Boolean {
    val isJobNonTerminal = (job.status == JobStatus.CREATED).or(job.status == JobStatus.RUNNING)
    val isJobRecurring = (job.type == JobType.RECURRING)
    return (isJobRecurring && isJobNonTerminal)
      .also { logger.info { "isJobRecurring : $isJobRecurring && isJobNonTerminal : $isJobNonTerminal = shouldCreateNextJob : $it" } }
  }
}