package com.example.dtos.countries

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern


data class GetCountryStatsDTO(
    @field:NotNull(message = "startDate must not be null")
    @field:Pattern(
        regexp = "\\d{4}-\\d{2}-\\d{2}",
        message = "startDate must be in the format YYYY-MM-DD"
    )
    // FIX: it's better that class field will have already specific type which is required for next actions
    // LocalDate could work good for it
    val startDate: String?,

    @field:NotNull(message = "endDate must not be null")
    @field:Pattern(
        regexp = "\\d{4}-\\d{2}-\\d{2}",
        message = "endDate must be in the format YYYY-MM-DD"
    )
    val endDate: String?,

    @field:Pattern(
        regexp = "true|false",
        message = "groupLocal must be either true or false"
    )
    val groupLocal: String? = "false"
)
