package karya.data.psql.di

import dagger.BindsInstance
import dagger.Component
import karya.core.repos.*
import karya.data.psql.configs.PsqlRepoConfig
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    PsqlModule::class,
    PsqlUtilsModule::class,
  ],
)
interface PsqlComponent {
  val plansRepo: PlansRepo
  val usersRepo: UsersRepo
  val tasksRepo: TasksRepo
  val errorLogsRepo: ErrorLogsRepo

  val repoConnector: RepoConnector

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun config(psqlRepoConfig: PsqlRepoConfig): Builder

    fun build(): PsqlComponent
  }
}
