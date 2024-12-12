package com.example

import com.example.config.Database
import com.example.di.AppModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun Application.module() {
    val injector: Injector = Guice.createInjector(AppModule())
    val dataSource = injector.getInstance(javax.sql.DataSource::class.java)

    Database.connectAndInit(dataSource)

    configureSerialization()
    configureSecurity()
    configureRouting(injector)
}

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}
