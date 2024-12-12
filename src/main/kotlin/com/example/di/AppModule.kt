package com.example.di

import DatabaseConfig
import com.example.entities.GeoDataEntity
import com.example.respositories.GeoDataRepository
import com.example.routes.CountryRouter
import com.example.services.GeoDataService
import com.example.utils.BatchSaver
import com.google.inject.AbstractModule
import javax.sql.DataSource
import com.google.inject.Provides
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.validation.Validation
import jakarta.validation.Validator

class AppModule : AbstractModule() {
    override fun configure() {
        bind(GeoDataRepository::class.java).asEagerSingleton()
        bind(GeoDataService::class.java).asEagerSingleton()
        bind(Validator::class.java).toInstance(Validation.buildDefaultValidatorFactory().validator)
        bind(CountryRouter::class.java)
    }

    @Provides
    fun provideDatabaseConfig(): DatabaseConfig {
        return DatabaseConfig.load() // Load from `application.conf`
    }

    @Provides
    fun provideDataSource(config: DatabaseConfig): DataSource {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://${config.host}/${config.name}"
            username = config.username
            password = config.password
        }
        return HikariDataSource(hikariConfig)
    }


    @Provides
    @Singleton
    fun provideBatchSaver(repository: GeoDataRepository): BatchSaver<GeoDataEntity> {
        return BatchSaver(saveFunction = repository::saveBatch).apply { start() }
    }

}