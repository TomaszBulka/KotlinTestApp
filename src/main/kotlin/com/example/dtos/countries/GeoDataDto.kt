import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import kotlinx.serialization.Serializable

@Serializable
data class GeoDataDTO(
    @field:NotBlank(message = "Country must not be blank")
    @field:Pattern(
        regexp = "^[a-zA-Z\\s]{2,100}\$",
        message = "Country must be a valid string with 2-100 alphabetic characters"
    )
    val country: String,

    @field:Positive(message = "Timestamp must be a positive number")
    val timestamp: Long,

    @field:Positive(message = "User ID must be a positive integer")
    val userId: Int
)