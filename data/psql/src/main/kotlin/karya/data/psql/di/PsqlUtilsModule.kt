package karya.data.psql.di

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class PsqlUtilsModule {

  @Provides
  @Singleton
  fun provideFlyway(@Named("FLYWAY") properties: Properties) : Flyway {
    return Flyway.configure()
      .dataSource(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"))
      .locations("classpath:db/migrations")
      .baselineOnMigrate(true)
      .placeholderReplacement(true)
      .load()
  }

  @Provides
  @Singleton
  fun provideHikariDataSource(@Named("HIKARI") properties: Properties) : HikariDataSource {
    val hikariConfig = HikariConfig(properties)
    return HikariDataSource(hikariConfig)
  }

  @Provides
  @Singleton
  fun provideDatabase(hikariDataSource: HikariDataSource) : Database {
    return Database.connect(hikariDataSource)
  }
}