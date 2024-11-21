package karya.data.psql.di

import dagger.BindsInstance
import dagger.Component
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.core.repos.RepoConnector
import karya.data.psql.configs.PsqlRepoConfig
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    PsqlModule::class,
    PsqlUtilsModule::class
  ]
)
interface PsqlComponent {

  val jobsRepo : JobsRepo
  val usersRepo : UsersRepo
  val tasksRepo : TasksRepo

  val repoConnector : RepoConnector

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(psqlRepoConfig: PsqlRepoConfig) : Builder

    fun build() : PsqlComponent
  }
}