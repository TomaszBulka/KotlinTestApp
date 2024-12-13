package com.example

import com.example.routes.CountryRouter
import com.example.routes.userRouting
import com.example.services.JwtService
import com.google.inject.Injector
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting(injector: Injector) {
    val countryRouter = injector.getInstance(CountryRouter::class.java)
    val jwtService = injector.getInstance(JwtService::class.java)

    routing {
        with(countryRouter) {
            registerRoutes()
        }
        userRouting(jwtService)
    }
}
