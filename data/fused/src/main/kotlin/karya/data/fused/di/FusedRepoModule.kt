package karya.data.fused.di

import dagger.Module
import dagger.Provides
import karya.core.configs.RepoConfig
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.core.repos.RepoConnector
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.fused.utils.ReposWrapper
import karya.data.psql.configs.PsqlRepoConfig
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
    is PsqlRepoConfig -> providePsqlRepoWrapper(repoConfig)

    else -> throw UnknownProviderException("repo", repoConfig.provider)
  }

  private fun providePsqlRepoWrapper(psqlConfig: PsqlRepoConfig): ReposWrapper {
    val component = DaggerPsqlComponent.builder()
      .config(psqlConfig)
      .build()

    return ReposWrapper(
      jobsRepo = component.jobsRepo,
      usersRepo = component.usersRepo,
      tasksRepo = component.tasksRepo,
      repoConnector = component.repoConnector
    )
  }

}