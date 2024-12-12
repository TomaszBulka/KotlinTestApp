package com.example.utils

import com.example.entities.GeoDataEntity
import com.example.respositories.GeoDataRepository
import com.google.inject.Inject
import com.google.inject.Provider

class BatchSaverProvider @Inject constructor(
    private val repository: GeoDataRepository
) : Provider<BatchSaver<GeoDataEntity>> {
    override fun get(): BatchSaver<GeoDataEntity> {
        return BatchSaver<GeoDataEntity>(
            saveFunction = repository::saveBatch
        ).apply { start() }
    }
}