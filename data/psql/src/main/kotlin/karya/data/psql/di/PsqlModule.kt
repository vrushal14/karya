package karya.data.psql.di

import dagger.Binds
import dagger.Module
import karya.core.repos.JobsRepo
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.data.psql.repos.PsqlJobsRepo
import karya.data.psql.repos.PsqlRepoConnector
import karya.data.psql.repos.PsqlTasksRepo
import karya.data.psql.repos.PsqlUsersRepo
import javax.inject.Singleton

@Module
abstract class PsqlModule {
    @Binds
    @Singleton
    abstract fun provideUsersRepo(psqlUsersRepo: PsqlUsersRepo): UsersRepo

    @Binds
    @Singleton
    abstract fun provideTasksRepo(psqlTasksRepo: PsqlTasksRepo): TasksRepo

    @Binds
    @Singleton
    abstract fun provideJobsRepo(psqlJobsRepo: PsqlJobsRepo): JobsRepo

    @Binds
    @Singleton
    abstract fun provideRepoConnector(psqlRepoConnector: PsqlRepoConnector): RepoConnector
}
