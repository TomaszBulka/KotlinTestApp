package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime


object GeoData: Table() {
    val id = integer("id").autoIncrement()
    val country = varchar("country", 100)
    val ip = varchar("ip", 39)
    val timestamp = datetime("timestamp")
    val user_id = reference("user_id", User.id, onDelete = ReferenceOption.CASCADE)


    override val primaryKey = PrimaryKey(id)
}