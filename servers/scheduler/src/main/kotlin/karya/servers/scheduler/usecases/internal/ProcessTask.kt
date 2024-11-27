package karya.servers.scheduler.usecases.internal

import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.JobType
import karya.core.entities.enums.TaskStatus
import karya.core.exceptions.JobException.JobNotFoundException
import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ProcessTask
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val jobsRepo: JobsRepo,
  private val locksClient: LocksClient,
  private val manageTasks: ManageTasks,
) {
  companion object : Logging

  suspend fun invoke(task: Task): Boolean {
    val result = locksClient.withLock(task.id) { processTaskInternal(task) }
    return when (result) {
      is LockResult.Success -> true
      is LockResult.Failure -> false
    }
  }

  private suspend fun processTaskInternal(task: Task) {
    logger.info("[${getInstanceName()}] : [PROCESSING TASK] --- TaskId : ${task.id}")
    val job = jobsRepo.get(task.jobId) ?: throw JobNotFoundException(task.jobId)
    updateTaskStatus(job, task)

    val updatedJob = transitionJobStatus(job)
    if (shouldCreateNextTask(job)) manageTasks.invoke(updatedJob, task)
  }

  private suspend fun updateTaskStatus(job: Job, task: Task) = when (job.status) {
    JobStatus.CREATED, JobStatus.RUNNING -> TaskStatus.PROCESSING
    JobStatus.COMPLETED -> TaskStatus.SUCCESS
    JobStatus.CANCELLED -> TaskStatus.CANCELLED
  }.also {
    if (task.status != it) {
      tasksRepo.updateStatus(task.id, it)
      logger.info("[${getInstanceName()}] : Transitioning task status from : ${task.status} -> $it")
    }
  }

  private suspend fun transitionJobStatus(job: Job) =
    when (job.status) {
      JobStatus.CREATED ->
        job
          .copy(status = JobStatus.RUNNING)
          .also { jobsRepo.updateStatus(job.id, JobStatus.RUNNING) }
          .also { logger.info("[${getInstanceName()}] : Transitioning job status from : ${job.status} -> ${it.status}") }

      else -> job
    }

  private suspend fun shouldCreateNextTask(job: Job): Boolean {
    val isJobNonTerminal = (job.status == JobStatus.CREATED).or(job.status == JobStatus.RUNNING)
    val isJobRecurring = (job.type == JobType.RECURRING)
    return (isJobRecurring && isJobNonTerminal)
      .also {
        logger.info(
          "[${getInstanceName()}] : isJobRecurring : $isJobRecurring && isJobNonTerminal : $isJobNonTerminal = shouldCreateNextJob : $it"
        )
      }
  }
}
