package karya.data.psql.di

import karya.data.psql.configs.PsqlRepoConfig

object PsqlComponentFactory {
    fun build(config: PsqlRepoConfig) =
        DaggerPsqlComponent
            .builder()
            .config(config)
            .build()
}
