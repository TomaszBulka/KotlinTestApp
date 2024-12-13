import com.typesafe.config.ConfigFactory

data class DatabaseConfig(
    val host: String,
    val username: String,
    val password: String,
    val name: String
) {
    companion object {
        fun load(config: Map<String, String>): DatabaseConfig {

            return DatabaseConfig(
                host = config["dbHost"] as String,
                username = config["dbUsername"] as String,
                password = config["dbPassword"] as String,
                name = config["dbName"] as String
            )
        }
    }
}
