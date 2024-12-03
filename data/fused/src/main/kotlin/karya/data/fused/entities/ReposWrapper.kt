package karya.data.fused.entities

import karya.core.repos.*

data class ReposWrapper(
  val usersRepo: UsersRepo,
  val plansRepo: PlansRepo,
  val tasksRepo: TasksRepo,
  val errorLogsRepo: ErrorLogsRepo,
  val repoConnector: RepoConnector,
)
