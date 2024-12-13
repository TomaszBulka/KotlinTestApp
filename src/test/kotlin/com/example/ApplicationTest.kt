package com.example

import com.example.di.AppModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        val injector: Injector = Guice.createInjector(AppModule())
        application {
            module(injector)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
