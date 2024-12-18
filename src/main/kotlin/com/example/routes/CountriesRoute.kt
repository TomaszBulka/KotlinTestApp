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
                    // FIX: use toString() method rather than casting to String
                    val startDateParam = call.request.queryParameters["startDate"] as String
                    val endDateParam = call.request.queryParameters["endDate"] as String
                    val groupLocalParam = call.request.queryParameters["groupLocal"] as String

                    try {
                        // FIX: where you use this dto? seems only for validation?
                        val dto = GetCountryStatsDTO(startDateParam, endDateParam, groupLocalParam)
                        val violations = validator.validate(dto)

                        if (violations.isNotEmpty()) {
                            // FIX: it's a bad practise to throw exception in case of non exceptional case
                            // here we could just respond with errors and return
                            throw ConstraintViolationException(violations)
                        }

                    } catch (e: ConstraintViolationException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("errors" to e.constraintViolations.map { it.message })
                        )
                    }

                    val geoData = geoDataService.getGeoDataByCountry(startDateParam, endDateParam, groupLocalParam)

                    if (geoData.isEmpty()) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.OK, geoData)
                    }
                    // FIX: for any exception, it's better to catch just Exception, because Error could be related to JVM
                    // errors and don't need to catch these cases at all
                } catch (e: Error) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf("message" to "An unexpected error occurred. Please try again later.")
                    )
                }
            }

            post("/geodata") {
                try {
                    val geoDataRequest = try {
                        call.receive<GeoDataDTO>()
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                        return@post
                    }

                    // FIX: This parameter is not used
                    val localDateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(geoDataRequest.timestamp),
                        ZoneOffset.UTC
                    )
                    val clientIp = call.request.origin.remoteAddress

                    geoDataService.saveGeoData(geoDataRequest, clientIp)

                    call.respond(HttpStatusCode.Accepted, "GeoData added to batch queue")
                } catch (e: Error) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf("message" to "An unexpected error occurred. Please try again later.")
                    )
                }
            }
        }
    }
}


