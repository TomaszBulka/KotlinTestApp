package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JwtService (
    private val jwtAudience: String,
    private val jwtDomain: String,
    private val jwtSecret: String,
) {
    private val tokenValidityMillis = 24 * 60 * 60 * 1000

    fun generateJwtToken(email: String): String {
        val algorithm = Algorithm.HMAC256(jwtSecret)

        return JWT.create()
            .withIssuer(jwtDomain)
            .withAudience(jwtAudience)
            .withSubject(email)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + tokenValidityMillis))
            .sign(algorithm)
    }
}
