package karya.data.fused.di

import dagger.Module
import dagger.Provides
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.core.repos.RepoConnector
import karya.data.fused.repos.RepoConfig
import karya.data.fused.repos.ReposWrapper
import karya.data.psql.di.DaggerPsqlComponent
import javax.inject.Singleton

@Module
class FusedRepoModule {

  @Provides
  @Singleton
  fun provideRepoConnector(wrapper: ReposWrapper): RepoConnector = wrapper.repoConnector

  @Provides
  @Singleton
  fun provideUsersRepo(wrapper: ReposWrapper): UsersRepo = wrapper.usersRepo

  @Provides
  @Singleton
  fun provideJobsRepo(wrapper: ReposWrapper): JobsRepo = wrapper.jobsRepo

  @Provides
  @Singleton
  fun provideTasksRepo(wrapper: ReposWrapper): TasksRepo = wrapper.tasksRepo

  @Provides
  @Singleton
  fun provideReposWrapper(repoConfig: RepoConfig) : ReposWrapper = when(repoConfig) {
    is RepoConfig.Psql -> providePsqlRepoWrapper(repoConfig)
  }

  private fun providePsqlRepoWrapper(psqlConfig: RepoConfig.Psql): ReposWrapper {
    val component = DaggerPsqlComponent.builder()
      .hikariProperties(psqlConfig.hikariProperties)
      .flywayProperties(psqlConfig.flywayProperties)
      .partitions(psqlConfig.partitions)
      .build()

    return ReposWrapper(
      jobsRepo = component.jobsRepo,
      usersRepo = component.usersRepo,
      tasksRepo = component.tasksRepo,
      repoConnector = component.repoConnector
    )
  }

}