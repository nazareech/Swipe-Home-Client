package com.nazar_protasov.swipehome.network

import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import com.nazar_protasov.swipehome.ui.models.Property
import com.nazar_protasov.swipehome.ui.models.PropertyDetails
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
    @SerialName("creates_at") val creates_at: String? = null
)

// Функція-розширення для мапінгу
fun PropertyDTO.toUIProperty(): Property {
    // Формуємо повний URL для зображення
    val rawImages = this.images_map?.keys?.toList() ?: emptyList()

    val imageUrls = if (rawImages.isNotEmpty()) {
        // Додаємо /uploads/, оскільки сервер зазвичай віддає статику за цим префіксом
        rawImages.map{ "${PropertyApiService.BASE_URL}/uploads$it" }
    } else {
        listOf("${PropertyApiService.BASE_URL}/uploads/") // Заглушка якщо фото немає
    }

    val formattedPrice = "$${this.price.toInt()}"

    return Property(
        id = this.id_property?.toString() ?: "0",
        imagesUrl = imageUrls,
        price = formattedPrice,
        title = this.title,
        location = this.localization,
        rooms = this.rooms,
        area = this.area,
        buildingType = this.building_type
    )
}
// Функція-розширення для мапінгу
fun PropertyDTO.toUIDetailsProperty(): PropertyDetails {
    // Формуємо повний URL для зображення
    val rawImages = this.images_map?.keys?.toList() ?: emptyList()

    val imageUrls = if (rawImages.isNotEmpty()) {
        // Додаємо /uploads/, оскільки сервер зазвичай віддає статику за цим префіксом
        rawImages.map{ "${PropertyApiService.BASE_URL}/uploads$it" }
    } else {
        listOf("${PropertyApiService.BASE_URL}/uploads/") // Заглушка якщо фото немає
    }

    val formattedPrice = "$${this.price}"

    val formatedCreatedAt = this.creates_at?.split("T")?.get(0) ?: ""

    return PropertyDetails(
        id_property = this.id_property ?: 0,
        id_owner = this.id_owner,
        title = this.title,
        description = this.description,
        localization = this.localization,
        price = formattedPrice,
        area = this.area.toString(),
        rooms = this.rooms.toString(),
        category = this.category,
        subCategory = this.subCategory,
        parking = this.parking,
        pets_allowed = this.pets_allowed,
        elevator = this.elevator,
        furniture = this.furniture,
        building_type = this.building_type,
        status = this.status,
        imagesUrl = imageUrls,
        creates_at = formatedCreatedAt
    )
}

class PropertyApiService(private val client: io.ktor.client.HttpClient){
    companion object {
        const val BASE_URL = "http://192.168.0.78:8080"
    }

    suspend fun fetchProperties(
        token: String,
        filterRequest: FilterRequestDTO
    ): List<PropertyDTO> {
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

            // Повертаємо список DTO як є
            response.properties
        } catch (e: Exception) {
            println("Error fetching properties: ${e.message}")
            emptyList() // Повертаємо пустий список у разі помилки
        }
    }
}
