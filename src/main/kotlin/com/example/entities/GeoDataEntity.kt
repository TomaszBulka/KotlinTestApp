package com.example.entities

import java.time.LocalDateTime

data class GeoDataEntity(
    val country: String,
    val userId: Int,
    val timestamp: LocalDateTime,
    val ip: String
)