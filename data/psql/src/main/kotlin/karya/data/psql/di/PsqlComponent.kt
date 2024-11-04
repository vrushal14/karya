package karya.data.psql.di

import dagger.BindsInstance
import dagger.Component
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.core.repos.RepoConnector
import java.util.Properties
import javax.inject.Named
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
    fun partitions(partitions: Int) : Builder

    @BindsInstance
    fun hikariProperties(@Named("HIKARI") properties: Properties) : Builder

    @BindsInstance
    fun flywayProperties(@Named("FLYWAY") properties: Properties) : Builder

    fun build() : PsqlComponent
  }
}