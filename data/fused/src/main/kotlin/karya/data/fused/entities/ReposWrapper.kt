package karya.data.fused.entities

import karya.core.repos.ErrorLogsRepo
import karya.core.repos.JobsRepo
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo

data class ReposWrapper(
  val usersRepo: UsersRepo,
  val jobsRepo: JobsRepo,
  val tasksRepo: TasksRepo,
  val errorLogsRepo: ErrorLogsRepo,
  val repoConnector: RepoConnector,
)
