package com.nazar_protasov.swipehome.network

import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import com.nazar_protasov.swipehome.ui.models.Property
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyResponseDTO(
    val properties: List<PropertyDTO>
)

@Serializable
data class PropertyDTO(
    val id_property: Int? = null,
    val id_owner: Int,
    val title: String,
    val description: String,
    val localization: String,
    val price: Double,
    val area: Double,
    val rooms: Int,
    val category: String,
    @SerialName("subcategory") val subCategory: String,
    val parking: String,
    val pets_allowed: Boolean,
    val elevator: Boolean,
    val furniture: Boolean,
    val building_type: String,
    val status: String,
    val images_map: Map<String, Boolean>? = null,
    @SerialName("creates_at") val created_at: String? = null
)

// Функція-розширення для мапінгу
fun PropertyDTO.toUIProperty(): Property {
    // Формуємо повний URL для зображення
    val rawImage = this.images_map?.entries?.firstOrNull { it.value }?.key
        ?: this.images_map?.keys?.firstOrNull()

    val mainImage = if (rawImage != null) {
        // Додаємо /uploads/, оскільки сервер зазвичай віддає статику за цим префіксом
        "${PropertyApiService.BASE_URL}/uploads$rawImage"
    } else {
        "${PropertyApiService.BASE_URL}/uploads/" // Заглушка
    }

    val formattedPrice = "$${this.price.toInt()}"

    return Property(
        id = this.id_property?.toString() ?: "0",
        imageUrl = mainImage,
        price = formattedPrice,
        title = this.title,
        location = this.localization,
        rooms = this.rooms,
        area = this.area,
        buildingType = this.building_type
    )
}

class PropertyApiService(private val client: io.ktor.client.HttpClient){
    companion object {
        const val BASE_URL = "http://10.14.0.242:8080"
    }

    suspend fun fetchProperties(
        token: String,
        filterRequest: FilterRequestDTO
    ): List<Property> {
        return try {
            // Робимо POST-запит на бекенд
            val response: PropertyResponseDTO = client.post("${BASE_URL}/properties/fetch"){
                // Додаємо Content-Type: application/json
                contentType(ContentType.Application.Json)

                // Додаємо Authorization заголовок
                header("Authorization", "Bearer $token")

                // Передаємо тіло запиту (DTO автоматично конвертується на JSON)
                setBody(filterRequest)
            }.body()

            // Пропускаємо всі DTO через мапер
            response.properties.map { it.toUIProperty() }
        } catch (e: Exception) {
            println("Error fetching properties: ${e.message}")
            emptyList() // Повертаємо пустий список у разі помилки (наприклад, сервер вимкнено)
        }
    }
}
