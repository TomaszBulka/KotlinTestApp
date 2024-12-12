import com.typesafe.config.ConfigFactory

data class DatabaseConfig(
    val host: String,
    val username: String,
    val password: String,
    val name: String
) {
    companion object {
        fun load(): DatabaseConfig {
            val config = ConfigFactory.load("application.conf").getConfig("database")

            return DatabaseConfig(
                host = config.getString("host"),
                username = config.getString("username"),
                password = config.getString("password"),
                name = config.getString("name")
            )
        }
    }
}
