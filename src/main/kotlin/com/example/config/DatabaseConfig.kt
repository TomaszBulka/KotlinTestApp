import com.typesafe.config.ConfigFactory

data class DatabaseConfig(
    val host: String,
    val username: String,
    val password: String,
    val name: String
) {
    // FIX: move this method to some utils, don't need to have any complex logic in data classes
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
