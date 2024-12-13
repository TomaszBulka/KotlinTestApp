package com.example.routes

import com.example.dtos.users.UserInfoDTO
import com.example.services.JwtService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

fun Route.userRouting(jwtService: JwtService) {
    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

        post("/register") {
            val userInfo = try {
                call.receive<UserInfoDTO>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                return@post
            }

            val hashedPassword = BCrypt.hashpw(userInfo.password, BCrypt.gensalt())
            val jwtToken = jwtService.generateJwtToken(userInfo.email)

            transaction {
                User.insert {
                    it[email] = userInfo.email
                    it[password] = hashedPassword
                    it[access_token] = jwtToken
                }
            }


            call.respond(HttpStatusCode.Created, "GeoData inserted successfully")
        }


    post("/login") {
        val loginRequest =
            try {
                call.receive<UserInfoDTO>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                return@post
            }

        var jwtToken: String? = null
        var errorResponse: Pair<HttpStatusCode, String>? = null

        transaction {
            val user = User.selectAll().where{User.email eq loginRequest.email } .singleOrNull()

            if (user == null) {
                errorResponse = HttpStatusCode.Unauthorized to "Invalid email or password"
                return@transaction
            }

            val hashedPassword = user[User.password]

            if (!BCrypt.checkpw(loginRequest.password, hashedPassword)) {
                errorResponse = HttpStatusCode.Unauthorized to "Invalid email or password"
                return@transaction
            }

            jwtToken = jwtService.generateJwtToken(loginRequest.email)

            User.update({ User.email eq loginRequest.email }) {
                it[access_token] = jwtToken!!
            }
        }

        if (errorResponse != null) {
            call.respond(errorResponse!!.first, errorResponse!!.second)
        } else {
            call.respond(HttpStatusCode.OK, mapOf("token" to jwtToken!!))
        }
    }

}
