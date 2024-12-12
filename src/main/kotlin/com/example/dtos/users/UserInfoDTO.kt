package com.example.dtos.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

//@TODO change validation
@Serializable
data class UserInfoDTO (
    @field:NotNull(message = "email must not be null")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotNull(message = "Password must not be null")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
//    @field:Pattern(
//        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$",
//        message = "Password must contain at least one letter, one number, and one special character"
//    )
    val password: String,
)

