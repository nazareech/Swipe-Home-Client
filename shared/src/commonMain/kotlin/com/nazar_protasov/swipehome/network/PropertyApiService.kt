package com.nazar_protasov.swipehome.network

import com.nazar_protasov.swipehome.network.dto.AuthResponseDTO
import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import com.nazar_protasov.swipehome.network.dto.ForgotPasswordRequestDTO
import com.nazar_protasov.swipehome.network.dto.MessageResponseDTO
import com.nazar_protasov.swipehome.network.dto.RegisterRequestDTO
import com.nazar_protasov.swipehome.network.dto.ResetPasswordRequestDTO
import com.nazar_protasov.swipehome.network.dto.SwipeHistoryItemDTO
import com.nazar_protasov.swipehome.network.dto.SwipeHistoryResponseDTO
import com.nazar_protasov.swipehome.network.dto.SwipeRequestDTO
import com.nazar_protasov.swipehome.network.dto.VerifyCodeRequestDTO
import com.nazar_protasov.swipehome.ui.models.Property
import com.nazar_protasov.swipehome.ui.models.PropertyDetails
import com.nazar_protasov.swipehome.ui.utils.readBytesFromCache
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PropertyResponseDTO(
    val properties: List<PropertyDTO>
)

@Serializable
data class PropertyDTO(
    val idProperty: String? = null,
    val idOwner: String,
    val title: String,
    val description: String,
    val localization: String,
    val latitude: Double,
    val longitude: Double,
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
    // Формуємо повний URL для зображення, гарантуючи, що головне фото (true) перше
    val rawImages = this.imagesMap?.entries?.sortedByDescending { it.value }?.map { it.key } ?: emptyList()

    val imageUrls = if (rawImages.isNotEmpty()) {
        // Додаємо /uploads/, оскільки сервер зазвичай віддає статику за цим префіксом
        rawImages.map{ "${PropertyApiService.BASE_URL}/uploads/$it" }
    } else {
        listOf("${PropertyApiService.BASE_URL}/uploads/") // Заглушка якщо фото немає
    }

    val formattedPrice = "$${this.price.toInt()}"

    return Property(
        id = this.idProperty ?: "0",
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
    // Формуємо повний URL для зображення, гарантуючи, що головне фото (true) перше
    val rawImages = this.imagesMap?.entries?.sortedByDescending { it.value }?.map { it.key } ?: emptyList()

    val imageUrls = if (rawImages.isNotEmpty()) {
        // Додаємо /uploads/, оскільки сервер зазвичай віддає статику за цим префіксом
        rawImages.map{ "${PropertyApiService.BASE_URL}/uploads/$it" }
    } else {
        listOf("${PropertyApiService.BASE_URL}/uploads/photo_not_found.jpg") // Заглушка якщо фото немає
    }

    val formattedPrice = "$${this.price}"

    val formatedCreatedAt = this.createsAt?.split("T")?.get(0) ?: ""

    return PropertyDetails(
        idProperty = this.idProperty ?: "",
        idOwner = this.idOwner,
        title = this.title,
        description = this.description,
        localization = this.localization,
        lat = this.latitude,
        lon = this.longitude,
        price = formattedPrice,
        area = this.area.toString(),
        rooms = this.rooms.toString(),
        category = this.category,
        subCategory = this.subCategory,
        parking = this.parking,
        petsAllowed = this.petsAllowed,
        elevator = this.elevator,
        furniture = this.furniture,
        buildingType = this.buildingType,
        status = this.status,
        imagesUrl = imageUrls,
        createsAt = formatedCreatedAt
    )
}

class PropertyApiService(private val client: io.ktor.client.HttpClient){
    companion object {
        // adb reverse tcp:8080 tcp:8080
         const val BASE_URL = "http://127.0.0.1:8080"
//        const val BASE_URL = "https://profile-spout-nutmeg.ngrok-free.dev"
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
            if (e is CancellationException) throw e
            println("Error fetching properties: ${e.message}")
            e.printStackTrace()
            emptyList() // Повертаємо пустий список у разі помилки
        }
    }

    suspend fun fetchOwnProperties(
        token: String
    ): List<PropertyDTO> {
        return try {
            // Робимо GET-запит на бекенд
            val response: PropertyResponseDTO = client.get("${BASE_URL}/own/properties"){
                // Додаємо Authorization заголовок
                header("Authorization", "Bearer $token")
            }.body()

            // Повертаємо список DTO як є
            response.properties
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Error fetching properties: ${e.message}")
            emptyList() // Повертаємо пустий список у разі помилки
        }
    }
    suspend fun fetchProfileInfo(
        token: String
    ): CreateUserInfoResponses? {
        return try {
            // Робимо GET-запит на бекенд
            val response = client.get("${BASE_URL}/users/get/information"){
                // Додаємо Authorization заголовок
                header("Authorization", "Bearer $token")
            }

            if (response.status.isSuccess()) {
                response.body<CreateUserInfoResponses>()
            } else {
                println("Error fetching profile info: ${response.status}")
                null
            }

        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Error fetching profile: ${e.message}")
            null // Повертаємо пустий список у разі помилки
        }
    }
    suspend fun updatedProfile(token: String, profileRequest: UpdateProfileRequest): Boolean{
        return try {
            val response = client.post("${BASE_URL}/users/profile/update"){
                // Додаємо Content-Type: application/json
                contentType(ContentType.Application.Json)
                // Додаємо Authorization заголовок
                header("Authorization", "Bearer $token")
                // Передаємо тіло запиту (DTO автоматично конвертується на JSON)
                setBody(profileRequest)
            }

            response.status.isSuccess()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Помилка відправки оголошення: ${e.message}")
            false
        }
    }
    suspend fun updatedAvatar(token: String, photoBytes: ByteArray): UpdateAvatarRespond?{
        return try {
            val response = client.post("${BASE_URL}/users/profile/uploadAvatar"){
                // Додаємо Authorization заголовок
                header("Authorization", "Bearer $token")
                // Передаємо тіло запиту (DTO автоматично конвертується на JSON)
                setBody(MultiPartFormDataContent(formData {
                    append("avatar", photoBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(
                            HttpHeaders.ContentDisposition,
                            "form-data; name=\"avatar\"; filename=\"avatar.jpg\""
                        )
                    })
                }))
            }

            if (response.status.isSuccess()) {
                val result = response.body<UpdateAvatarRespond>()
                println("Аватар успішно оновлено: ${result.avatarUrl} ")
                result
            } else {
                val errorBody = response.body<String>()
                println("Помилка відправки фото для аватара: ${response.status} - $errorBody")
                null
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Помилка відправки фото для аватара: ${e.message}")
            null
        }
    }

    suspend fun createProperty(request: CreatePropertyRequest, photoPaths: List<String>, token: String): CreatePropertyResponse? {
        return try {
           val response = client.post("${BASE_URL}/properties/create"){
               header("Authorization", "Bearer $token")

                // Створюємо Multipart форму
                setBody(MultiPartFormDataContent(formData{

                    // Кодуємо об'єкт у JSON і кладемо його як текстове поле
                    val jsonString = Json.encodeToString(request)
                    append("property_data", jsonString)

                    // Якщо є фото, відправляємо, що перше - головне
                    if (photoPaths.isNotEmpty()) {
                        append("is_main", "true")
                    }

                    // Додаємо файли (тільки нові локальні фотографії)
                    photoPaths.forEachIndexed { index, path ->
                        // Відфільтровуємо шляхи, які вже є URL-адресами з сервера
                        if (!path.startsWith("http")) {
                            val imagesBytes = readBytesFromCache(path)
                            append("image", imagesBytes, Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "form-data; name=\"image\"; filename=\"photo_$index.jpg\"")
                            })
                        }
                    }
                })
                )
           }

            if(response.status.isSuccess()) {
                response.body<CreatePropertyResponse>()
            }else{
                val errorBody = response.body<String>()
                println("Помилка відправки оголошення: ${response.status} - $errorBody")
                null
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Помилка відправки оголошення: ${e.message}")
            null
        }
    }

    suspend fun deleteProperty(idProperty: String, token: String): String? {
        return try {
            val url = "${BASE_URL}/properties/${idProperty}"
            println("Спроба видалення оголошення. URL: $url")
            val response = client.delete(url) {
                header("Authorization", "Bearer $token")
            }

            println("Відповідь сервера при видаленні: ${response.status}")
            if(response.status.isSuccess()) {
                val text = response.body<String>() // Читаємо відповідь як текст, щоб уникнути помилки парсингу (вічного null)
                // Якщо сервер повертає JSON, спробуємо знайти в ньому повідомлення вручну, або просто повернемо весь текст
                text
            } else {
                val errorBody = response.body<String>()
                println("Помилка видалення оголошення: ${response.status} - $errorBody")
                null
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Error deleting property: ${e.message}")
            null
        }
    }

    suspend fun fetchSwipeHistory(token: String): List<SwipeHistoryItemDTO> {
        return try {
            val response: SwipeHistoryResponseDTO = client.get("${BASE_URL}/swipes/history") {
                header("Authorization", "Bearer $token")
            }.body()
            response.history
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Error fetching swipe history: ${e.message}")
            emptyList()
        }
    }

    suspend fun sendSwipe(
        token: String,
        infoDTO: SwipeRequestDTO
    ): Boolean{
        return try {
            val url = "${BASE_URL}/properties/swipe"
            println("Спроба обробка свайпу: $url")
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(infoDTO)
            }

            println("Дані відправки: $response")
            response.status.isSuccess()
        } catch (e: Exception){
            if (e is CancellationException) throw e
            println("Помилка відправки свайпу: ${e.message}")
            false
        }
    }

    suspend fun registerUser(
        request: RegisterRequestDTO
    ): AuthResponseDTO? {
        return try {
            val response = client.post("${BASE_URL}/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                response.body<AuthResponseDTO>()
            } else {
                val errorBody = response.body<String>()
                println("Помилка реєстрації: ${response.status} - $errorBody")
                null
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Помилка мережі при реєстрації: ${e.message}")
            null
        }
    }

    suspend fun forgotPassword(
        emailOrLogin: String
    ): Boolean {
        return try {
            val response = client.post("${BASE_URL}/auth/forgot-password") {
                contentType(ContentType.Application.Json)
                setBody(ForgotPasswordRequestDTO(emailOrLogin = emailOrLogin))
            }

            if (response.status.isSuccess()) {
                true
            } else {
                val errorBody = response.body<String>()
                println("Помилка forgot-password: ${response.status} - $errorBody")
                false
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Помилка мережі при forgot-password: ${e.message}")
            false
        }
    }

    suspend fun verifyCode(
        emailOrLogin: String,
        code: String
    ): Result<String> {
        return try {
            val response = client.post("${BASE_URL}/auth/verify-code") {
                contentType(ContentType.Application.Json)
                setBody(VerifyCodeRequestDTO(emailOrLogin = emailOrLogin, code = code))
            }

            if (response.status.isSuccess()) {
                val msg = try {
                    response.body<MessageResponseDTO>().message
                } catch (_: Exception) {
                    response.body<String>()
                }
                Result.success(msg)
            } else {
                val errorBody = response.body<String>()
                println("Помилка verify-code: ${response.status} - $errorBody")
                Result.failure(Exception(errorBody.ifBlank { "Невірний або прострочений код" }))
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Помилка мережі при verify-code: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun resetPassword(
        emailOrLogin: String,
        code: Int,
        newPassword: String
    ): Boolean {
        return try {
            val response = client.post("${BASE_URL}/auth/reset-password") {
                contentType(ContentType.Application.Json)
                setBody(
                    ResetPasswordRequestDTO(
                        emailOrLogin = emailOrLogin,
                        code = code,
                        newPassword = newPassword
                    )
                )
            }

            if (response.status.isSuccess()) {
                true
            } else {
                val errorBody = response.body<String>()
                println("Помилка reset-password: ${response.status} - $errorBody")
                false
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("Помилка мережі при reset-password: ${e.message}")
            false
        }
    }
}
