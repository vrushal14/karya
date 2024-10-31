package karya.data.fused.repos

import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.core.connectors.RepoConnector

data class ReposWrapper(
  val usersRepo: UsersRepo,
  val jobsRepo: JobsRepo,
  val tasksRepo: TasksRepo,

  val repoConnector: RepoConnector
)
