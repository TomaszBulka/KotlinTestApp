package com.example.config

import User
import com.example.model.GeoData
import javax.sql.DataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Database {
    fun connectAndInit(dataSource: DataSource) {
        Database.connect(dataSource)
        initDatabase()
    }

    private fun initDatabase() {
        transaction {
            SchemaUtils.create(User, GeoData)
        }
    }
}

