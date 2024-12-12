package com.example.services

import GeoDataDTO
import com.example.entities.GeoDataEntity
import com.example.respositories.GeoDataRepository
import com.example.utils.BatchSaver
import com.google.inject.Inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class GeoDataService @Inject constructor(
    private val repository: GeoDataRepository,
    private val batchSaver: BatchSaver<GeoDataEntity>
) {
    fun saveGeoData(request: GeoDataDTO, clientIp: String) {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(request.timestamp),
            ZoneOffset.UTC
        )

        val geoData = GeoDataEntity(
            country = request.country,
            userId = request.userId,
            timestamp = localDateTime,
            ip = clientIp
        )

        batchSaver.addToBatch(geoData)
    }
}