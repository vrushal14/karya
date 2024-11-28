package karya.data.fused.di.components

import dagger.BindsInstance
import dagger.Component
import karya.core.configs.RepoConfig
import karya.core.repos.JobsRepo
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.data.fused.di.modules.FusedRepoModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    FusedRepoModule::class
  ],
)
interface FusedDataRepoComponent {

  val usersRepo: UsersRepo
  val jobsRepo: JobsRepo
  val tasksRepo: TasksRepo
  val repoConnector: RepoConnector

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun repoConfig(repoConfig: RepoConfig): Builder

    fun build(): FusedDataRepoComponent
  }
}

