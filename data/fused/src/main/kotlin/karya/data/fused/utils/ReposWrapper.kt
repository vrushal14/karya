package karya.data.fused.utils

import karya.core.repos.JobsRepo
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo

data class ReposWrapper(
	val usersRepo: UsersRepo,
	val jobsRepo: JobsRepo,
	val tasksRepo: TasksRepo,
	val repoConnector: RepoConnector,
)
