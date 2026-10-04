package com.nazar_protasov.swipehome.ui.screens.settings.listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.entity.MyListingEntity
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.network.PropertyDTO
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.ParkingType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.PropertyType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.VerificationMethods
import com.nazar_protasov.swipehome.ui.utils.getLocalizedMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MyListingsScreenModel(private val apiService: PropertyApiService, private val dao: SwipeHomeDao) : ScreenModel {
    val myListingProperties = dao.getMyListing()

    private val _properties = MutableStateFlow<List<PropertyDTO>>(emptyList())


    // Стан завантаження (щоб показувати крутилку)
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    // Канал для Snackbar на екрані списку
    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    // Стан саме для індикатора видалення
    var isDeleting by mutableStateOf(false)
        private set

    // TODO: Отримати реальний токен з сервера
    private val fakeUserToken = "d0d20eeb-2c54-4363-9b7b-0631ae0f9892"

    init{
        // При створенні екрана одразу йдемо на сервер
        fetchOwnProperties()
    }
    // Видалення чернетки (кнопка "Видалити" в TopBar)
    fun deleteListing(targetListingId: String) {
        // Якщо вже відправляється запит, повертаємось
        if (isDeleting) return

        screenModelScope.launch {
            isDeleting = true
            try {
                // Дістаємо картку з бази, щоб знати її serverId
                val card = dao.getCardById(targetListingId)
                if (card == null) {
                    println("Картку з ID $targetListingId не знайдено в локальній базі!")
                    return@launch
                }

                val serverIdToDelete = card.serverId
                println("serverIdToDelete = $serverIdToDelete")

                if(serverIdToDelete.isNullOrBlank()) {
                    dao.deleteCardById(id = targetListingId)
                    _snackbarEvent.send("Чернетку успішно видалено.")
                } else {

                    println("Відправляємо запит на сервер для видалення ID $serverIdToDelete")
                    // Відправляємо Запит видалення (Отримуємо об'єкт відповіді від сервера)
                    val serverResponse = withTimeoutOrNull(10.seconds) {
                        apiService.deleteProperty(serverIdToDelete, fakeUserToken)
                    }
                    if (serverResponse != null) {
                        println("Відповідь сервера: $serverResponse")
                        // Видаляємо локально, бо сервер успішно видалив (або змінив статус)
                        dao.deleteCardById(id = targetListingId)
                        _snackbarEvent.send("Успішно видалено: $serverResponse")
                    } else {
                        println("Відповідь сервера null (помилка або тайм-аут)")
                        _snackbarEvent.send("Помилка при видаленні з сервера.")
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі при видаленні: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                isDeleting = false
            }
        }
    }

    fun fetchOwnProperties(){
        screenModelScope.launch {
            _isLoading.value = true
            try {

                // Передаємо токен і поточні фільтри в API
                val result = withTimeoutOrNull(10.seconds) {
                    apiService.fetchOwnProperties(
                        token = fakeUserToken
                    )
                }

                if (result?.isEmpty() != null) {
                    println(result)

                    if (result.isNotEmpty()) {
                        result.forEach { dto ->
                            // Якщо це нова чернетка, генеруємо ID. Якщо вже редагуємо - залишаємо старий.
                            val currentDraftId = dto.idProperty ?: Random.nextInt(100000).toString()
                            // Room не вміє автоматично зберігати списки (List<String>)
                            // Тому конвертуємо список фото у рядок, розділений комами.
                            // Сортуємо так, щоб головне фото (true) було першим у списку
                            val photosString = dto.imagesMap?.entries
                                ?.sortedByDescending { it.value }?.joinToString(",") { it.key } ?: ""

                            val currentProperty = MyListingEntity(
                                localId = currentDraftId,
                                serverId = dto.idProperty,

                                // Безпечний мапінг статусу (якщо прийде "active", воно переведе його у верхній регістр)
                                listingStatus =
                                    try {
                                        ListingStatus.valueOf(dto.status.uppercase())
                                    } catch (e: Exception) {
                                        println("error: $e")
                                        ListingStatus.DRAFT
                                    },

                                title = dto.title,
                                description = dto.description,
                                address = dto.localization,
                                area = dto.area.toString(),
                                price = dto.price.toString(),
                                bedrooms = dto.rooms,
                                bathrooms = 1,
                                floorNumber = 1,
                                // Безпечний мапінг категорії нерухомості
                                propertyType =
                                    try {
                                        PropertyType.valueOf(dto.category.uppercase())
                                    } catch (e: Exception) {
                                        println("error: $e")
                                        PropertyType.APARTMENT
                                    },

                                allowedPets = dto.petsAllowed,
                                // Безпечний мапінг типу паркінгу
                                parkingType =
                                    try {
                                        ParkingType.valueOf(dto.parking.uppercase())
                                    } catch (e: Exception) {
                                        println("error: $e")
                                        ParkingType.NONE
                                    },
                                isFurnished = dto.furniture,
                                isBalcony = false,
                                isCondition = false,
                                gymIsSelect = false,
                                poolIsSelect = false,
                                elevatorIsSelect = dto.elevator,
                                securityIsSelect = false,
                                verificationMethod = VerificationMethods.DIIA,

                                photosUris = photosString,

                                likesNumber = 0,
                                viewsNumber = 0,
                                rejectsNumber = 0,
                                goToContactsNumber = 0,
                                createAt = dto.createsAt ?: "",
                                updateAt = dto.createsAt ?: ""
                            )
                            dao.saveListing(currentProperty)
                        }
                    }

                    // Викликаємо Ktor клієнт
                    _properties.value = result
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}