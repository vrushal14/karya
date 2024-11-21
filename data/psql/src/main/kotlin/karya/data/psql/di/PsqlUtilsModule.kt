package karya.data.psql.di

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import karya.data.psql.configs.PsqlRepoConfig
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.inject.Singleton

@Module
class PsqlUtilsModule {
	@Provides
	@Singleton
	fun provideFlyway(config: PsqlRepoConfig): Flyway {
		val properties = config.flywayProperties
		return Flyway
			.configure()
			.dataSource(
				properties.getProperty("url"),
				properties.getProperty("user"),
				properties.getProperty("password"),
			).locations("classpath:db/migrations")
			.baselineOnMigrate(true)
			.placeholderReplacement(true)
			.load()
	}

	@Provides
	@Singleton
	fun provideHikariDataSource(config: PsqlRepoConfig): HikariDataSource {
		val hikariConfig = HikariConfig(config.hikariProperties)
		return HikariDataSource(hikariConfig)
	}

	@Provides
	@Singleton
	fun provideDatabase(hikariDataSource: HikariDataSource): Database = Database.connect(hikariDataSource)
}
