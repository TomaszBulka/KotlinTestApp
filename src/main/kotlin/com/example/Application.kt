package com.example

import com.example.config.Database
import com.example.di.AppModule
import com.example.services.GeoDataService
import com.google.inject.Guice
import com.google.inject.Injector
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.concurrent.TimeUnit

fun Application.module(injector: Injector) {
    val dataSource = injector.getInstance(javax.sql.DataSource::class.java)

    Database.connectAndInit(dataSource)

    configureSerialization()
    configureSecurity()
    configureRouting(injector)
}

fun main(args: Array<String>) {
    val injector: Injector = Guice.createInjector(AppModule())

    embeddedServer(Netty, port = 8080) {
        module(injector)
    }.apply {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Shutting down services...")
            val geoDataService = injector.getInstance(GeoDataService::class.java)
            geoDataService.shutdown()
            stop(1000, 5000, TimeUnit.MILLISECONDS)
        })
    }.start(wait = true)
}
