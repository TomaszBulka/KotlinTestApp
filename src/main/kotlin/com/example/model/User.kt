import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object User : Table("user") {
    val id = integer("id").autoIncrement() // Primary Key
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 100)
    val access_token = varchar("access_token", 300)

    override val primaryKey = PrimaryKey(id)
}