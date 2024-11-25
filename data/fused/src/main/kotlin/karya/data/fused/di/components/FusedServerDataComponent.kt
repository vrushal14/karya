package karya.data.fused.di.components

import dagger.BindsInstance
import dagger.Component
import karya.core.configs.LocksConfig
import karya.core.configs.RepoConfig
import karya.core.locks.LocksClient
import karya.core.repos.JobsRepo
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.data.fused.di.modules.FusedLocksModule
import karya.data.fused.di.modules.FusedRepoModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    FusedRepoModule::class,
    FusedLocksModule::class,
  ],
)
interface FusedServerDataComponent {
  val usersRepo: UsersRepo
  val jobsRepo: JobsRepo
  val tasksRepo: TasksRepo

  val locksClient: LocksClient

  val repoConnector: RepoConnector

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun repoConfig(repoConfig: RepoConfig): Builder

    @BindsInstance
    fun locksConfig(locksConfig: LocksConfig): Builder

    fun build(): FusedServerDataComponent
  }
}
