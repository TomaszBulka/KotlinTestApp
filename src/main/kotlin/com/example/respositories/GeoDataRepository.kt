package com.example.respositories

import com.example.entities.GeoDataEntity
import com.example.model.GeoData
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

class GeoDataRepository {
    fun saveBatch(data: List<GeoDataEntity>) {
        transaction {
            GeoData.batchInsert(data) { geoData ->
                this[GeoData.country] = geoData.country
                this[GeoData.user_id] = geoData.userId
                this[GeoData.timestamp] = geoData.timestamp
                this[GeoData.ip] = geoData.ip
            }
        }
    }
}