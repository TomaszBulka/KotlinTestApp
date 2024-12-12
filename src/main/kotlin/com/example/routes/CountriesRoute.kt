package com.example.routes

import GeoDataDTO
import com.example.dtos.countries.GetCountryStatsDTO
import com.example.services.GeoDataService
import com.google.inject.Inject
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


class CountryRouter @Inject constructor(
    private val geoDataService: GeoDataService,
    private val validator: Validator
) {
    fun Route.registerRoutes() {
        authenticate("auth-jwt") {
            get("/countrystats") {
                try {
                    val startDateParam = call.request.queryParameters["startDate"]
                    val endDateParam = call.request.queryParameters["endDate"]
                    val groupLocalParam = call.request.queryParameters["groupLocal"]

                    val dto = GetCountryStatsDTO(startDateParam, endDateParam, groupLocalParam)
                    val violations = validator.validate(dto)

                    if (violations.isNotEmpty()) {
                        throw ConstraintViolationException(violations)
                    }

                } catch (e: ConstraintViolationException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("errors" to e.constraintViolations.map { it.message })
                    )
                }
            }

            post("/geodata") {
                val geoDataRequest = try {
                    call.receive<GeoDataDTO>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                    return@post
                }

                val localDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(geoDataRequest.timestamp),
                    ZoneOffset.UTC
                )
                val clientIp = call.request.origin.remoteAddress

                geoDataService.saveGeoData(geoDataRequest, clientIp)

                call.respond(HttpStatusCode.Accepted, "GeoData added to batch queue")
            }
        }
    }
}


