import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class GeoDataResponse(
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val country: String,
    val count: Int
)