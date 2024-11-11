package karya.servers.scheduler.usecases.external

import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.JobType
import karya.core.entities.enums.TaskStatus
import karya.core.exceptions.JobException.*
import karya.core.locks.LocksClient
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.servers.scheduler.usecases.internal.CreateNextTask
import karya.servers.scheduler.usecases.internal.PushTaskToQueue
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ProcessTask
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val jobsRepo: JobsRepo,
  private val locksClient: LocksClient,

  private val createNextTask: CreateNextTask,
  private val pushTaskToQueue: PushTaskToQueue
) {

  companion object : Logging

  suspend fun invoke(task: Task) {
    if(locksClient.getLock(task.id)) {
      val job = jobsRepo.get(task.jobId) ?: throw JobNotFoundException(task.jobId)
      val shouldPush = updateStatus(job, task)

      if (shouldCreateNextTask(job)) createNextTask.invoke(job)
      if (shouldPush) pushTaskToQueue.invoke(job, task)
      locksClient.freeLock(task.id)

    } else logger.warn("Unable to acquire lock on taskId --- ${task.id}")
  }

  private suspend fun updateStatus(job: Job, task: Task) : Boolean = when(job.status) {
    JobStatus.CREATED -> {
      jobsRepo.updateStatus(task.jobId, JobStatus.RUNNING)
      tasksRepo.updateStatus(task.id, TaskStatus.RUNNING)
      logger.info { "Updating Status : [Job | CREATED -> RUNNING] | [Task | CREATED -> RUNNING]" }
      true
    }
    JobStatus.RUNNING -> {
      tasksRepo.updateStatus(task.id, TaskStatus.RUNNING)
      logger.info { "Updating Status : [Job (No change) | RUNNING -> RUNNING] | [Task | CREATED -> RUNNING]" }
      true
    }

    JobStatus.CANCELLED, JobStatus.FAILURE, JobStatus.COMPLETED -> {
      tasksRepo.updateStatus(task.id, TaskStatus.CANCELLED)
      logger.info { "Updating Status : [Job (No change) | ${job.status} -> ${job.status}] | [Task | CREATED -> CANCELLED]" }
      false
    }
  }

  private fun shouldCreateNextTask(job: Job) : Boolean {
    val isJobNonTerminal = (job.status == JobStatus.CREATED).or(job.status == JobStatus.RUNNING)
    val isJobRecurring = (job.type == JobType.RECURRING)
    return (isJobRecurring && isJobNonTerminal)
      .also { logger.info { "isJobRecurring : $isJobRecurring && isJobNonTerminal : $isJobNonTerminal = shouldCreateNextJob : $it" } }
  }
}