package com.example.services

import GeoDataDTO
import GeoDataResponse
import com.example.entities.GeoDataEntity
import com.example.model.GeoData
import com.example.respositories.GeoDataRepository
import com.example.utils.BatchSaver
import com.google.inject.Inject
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.*

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

    fun getGeoDataByCountry(
        startDateParam: String,
        endDateParam: String,
        groupLocalParam: String
    ): List<GeoDataResponse> {
        val startDate = LocalDate.parse(startDateParam).atStartOfDay()
        val endDate = LocalDate.parse(endDateParam).atTime(LocalTime.MAX)
        val groupLocal = groupLocalParam.toBoolean()

        return if (groupLocal) {
            val rawData = transaction {
                GeoData
                    .select(GeoData.timestamp, GeoData.country).where { (GeoData.timestamp greaterEq startDate) and (GeoData.timestamp lessEq endDate) }
                    .toList()
            }

            rawData
                .groupBy { it[GeoData.timestamp] to it[GeoData.country] }
                .map { (key, value) ->
                    GeoDataResponse(
                        date = key.first,
                        country = key.second,
                        count = value.size
                    )
                }
                .sortedBy { it.date }
        } else {
            transaction {
                GeoData
                    .select(GeoData.timestamp, GeoData.country, GeoData.id.count()).where { (GeoData.timestamp greaterEq startDate) and (GeoData.timestamp lessEq endDate) }
                    .groupBy(GeoData.country, GeoData.timestamp)
                    .map {
                        GeoDataResponse(
                            date = it[GeoData.timestamp],
                            country = it[GeoData.country],
                            count = it[GeoData.id.count()].toInt()
                        )
                    }
                    .sortedBy { it.date }
            }
        }
    }

    fun shutdown() {
        batchSaver.stop()
    }
}