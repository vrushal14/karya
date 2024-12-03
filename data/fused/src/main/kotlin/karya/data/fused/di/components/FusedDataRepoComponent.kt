package karya.data.fused.di.components

import dagger.BindsInstance
import dagger.Component
import karya.core.configs.RepoConfig
import karya.core.repos.*
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
  val plansRepo: PlansRepo
  val tasksRepo: TasksRepo
  val errorLogsRepo: ErrorLogsRepo
  val repoConnector: RepoConnector

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun repoConfig(repoConfig: RepoConfig): Builder

    fun build(): FusedDataRepoComponent
  }
}

