package com.nazar_protasov.swipehome.network

import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import com.nazar_protasov.swipehome.ui.models.Property
import com.nazar_protasov.swipehome.ui.models.PropertyDetails
import com.nazar_protasov.swipehome.ui.utils.readBytesFromCache
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyResponseDTO(
    val properties: List<PropertyDTO>
)

@Serializable
data class PropertyDTO(
    val idProperty: Int? = null,
    val idOwner: Int,
    val title: String,
    val description: String,
    val localization: String,
    val price: Double,
    val area: Double,
    val rooms: Int,
    val category: String,
    @SerialName("subcategory") val subCategory: String,
    val parking: String,
    val petsAllowed: Boolean,
    val elevator: Boolean,
    val furniture: Boolean,
    val buildingType: String,
    val status: String,
    val imagesMap: Map<String, Boolean>? = null,
    @SerialName("creates_at") val createsAt: String? = null
)

// Функція-розширення для мапінгу
fun PropertyDTO.toUIProperty(): Property {
    // Формуємо повний URL для зображення
    val rawImages = this.imagesMap?.keys?.toList() ?: emptyList()

    val imageUrls = if (rawImages.isNotEmpty()) {
        // Додаємо /uploads/, оскільки сервер зазвичай віддає статику за цим префіксом
        rawImages.map{ "${PropertyApiService.BASE_URL}/uploads$it" }
    } else {
        listOf("${PropertyApiService.BASE_URL}/uploads/") // Заглушка якщо фото немає
    }

    val formattedPrice = "$${this.price.toInt()}"

    return Property(
        id = this.idProperty?.toString() ?: "0",
        imagesUrl = imageUrls,
        price = formattedPrice,
        title = this.title,
        location = this.localization,
        rooms = this.rooms,
        area = this.area,
        buildingType = this.buildingType
    )
}
// Функція-розширення для мапінгу
fun PropertyDTO.toUIDetailsProperty(): PropertyDetails {
    // Формуємо повний URL для зображення
    val rawImages = this.imagesMap?.keys?.toList() ?: emptyList()

    val imageUrls = if (rawImages.isNotEmpty()) {
        // Додаємо /uploads/, оскільки сервер зазвичай віддає статику за цим префіксом
        rawImages.map{ "${PropertyApiService.BASE_URL}/uploads$it" }
    } else {
        listOf("${PropertyApiService.BASE_URL}/uploads/") // Заглушка якщо фото немає
    }

    val formattedPrice = "$${this.price}"

    val formatedCreatedAt = this.createsAt?.split("T")?.get(0) ?: ""

    return PropertyDetails(
        id_property = this.idProperty ?: 0,
        id_owner = this.idOwner,
        title = this.title,
        description = this.description,
        localization = this.localization,
        price = formattedPrice,
        area = this.area.toString(),
        rooms = this.rooms.toString(),
        category = this.category,
        subCategory = this.subCategory,
        parking = this.parking,
        pets_allowed = this.petsAllowed,
        elevator = this.elevator,
        furniture = this.furniture,
        building_type = this.buildingType,
        status = this.status,
        imagesUrl = imageUrls,
        creates_at = formatedCreatedAt
    )
}

class PropertyApiService(private val client: io.ktor.client.HttpClient){
    companion object {
        const val BASE_URL = "http://192.168.1.2:8080"
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

    suspend fun createProperty(request: CreatePropertyRequest, token: String): String? {
        return try {
           val response = client.post("${BASE_URL}/properties/create"){
               header("Authorization", "Bearer $token")
               contentType(ContentType.Application.Json)
               setBody(request)
           }

            if(response.status.isSuccess()) {
                val responseBody = response.body<CreatePropertyResponse>()
                responseBody.idProperty
            }else{
                val errorBody = response.body<String>()
                println("Помилка відправки оголошення: ${response.status} - $errorBody")
                null
            }
        } catch (e: Exception) {
            println("Помилка відправки оголошення: ${e.message}")
            null
        }
    }

    suspend fun uploadImage(propertyId: String, photoPaths: List<String>,  token: String): Boolean {
        return try {
            val response = client.post("${BASE_URL}/properties/images/upload") {
                header("Authorization", "Bearer $token")
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            // Передаємо propertyId
                            append("id_property", propertyId)

                            // Проходимось по всіх фотографіях
                            photoPaths.forEachIndexed { index, path ->
                                // Перше фото (index == 0) завжди головне
                                val isMain = if (index == 0) "true" else "false"
                                append("is_main", isMain)

                                // Читаємо файл і додаємо його як частину форми
                                val imagesBytes = readBytesFromCache(path)
                                append("image", imagesBytes, Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "form-data; name=\"image\"; filename=\"photo_$index.jpg\"")
                                })
                            }
                        }
                    )
                )
            }
            if (response.status.isSuccess()) {
                val responseBody = response.body<UploadImagesResponse>()
                println("Фото завантажено: ${responseBody.message}")
                responseBody.images.isNotEmpty()
            } else {
                val errorBody = response.body<String>()
                println("Помилка завантаження фото: ${response.status} - $errorBody")
                false
            }
        } catch (e: Exception) {
            println("Помилка завантаження фото: ${e.message}")
            false
        }
    }
}
