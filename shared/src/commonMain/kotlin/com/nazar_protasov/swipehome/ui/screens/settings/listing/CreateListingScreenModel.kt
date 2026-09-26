package com.nazar_protasov.swipehome.ui.screens.settings.listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.entity.MyListingEntity
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao
import com.nazar_protasov.swipehome.network.CreatePropertyRequest
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.PropertyType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.VerificationMethods
import com.nazar_protasov.swipehome.ui.utils.getLocalizedMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

class CreateListingScreenModel(
    private val dao: SwipeHomeDao,
    private val apiService: PropertyApiService
): ScreenModel {
    // ID чернетки, якщо ми редагуємо існуючу, або null, якщо це нове оголошення
    var listingId by mutableStateOf<String?>(null)

    var currentStep by mutableStateOf(1)
    val totalSteps = 4

    var selectedType by mutableStateOf(PropertyType.APARTMENT)
    var address by mutableStateOf("")
    var area by mutableStateOf("")
    var price by mutableStateOf("")

    var countBedrooms by mutableStateOf(1)
    var countBathrooms by mutableStateOf(1)
    var floorNumber by mutableStateOf(1)

    var allowedPets by mutableStateOf(false)
    var isParking by mutableStateOf(false)
    var isFurnished by mutableStateOf(false)
    var isBalcony by mutableStateOf(false)
    var isCondition by mutableStateOf(false)

    var description by mutableStateOf("")
    var title by mutableStateOf("")
    var gymIsSelect by mutableStateOf(false)
    var poolIsSelect by mutableStateOf(false)
    var elevatorIsSelect by mutableStateOf(false)
    var securityIsSelect by mutableStateOf(false)

    val uploadedPhotos = mutableStateListOf<String>()

    var verificationMethod by mutableStateOf(VerificationMethods.DIIA)

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    var isLoading by mutableStateOf(false)
    private set // UI може читати, але не змінювати

    // Завантаження існуючої чернетки
    fun loadDraft(draftId: String) {
        screenModelScope.launch {
            val draft = dao.getCardById(draftId)

            if (draft != null){
                this@CreateListingScreenModel.listingId = draft.localId
                this@CreateListingScreenModel.selectedType = draft.propertyType
                this@CreateListingScreenModel.address = draft.address
                this@CreateListingScreenModel.area = draft.area
                this@CreateListingScreenModel.price = draft.price
                this@CreateListingScreenModel.countBedrooms = draft.bedrooms
                this@CreateListingScreenModel.countBathrooms = draft.bathrooms
                this@CreateListingScreenModel.title = draft.title
                this@CreateListingScreenModel.description = draft.description
                this@CreateListingScreenModel.floorNumber = draft.floorNumber
                this@CreateListingScreenModel.allowedPets = draft.allowedPets
                this@CreateListingScreenModel.isParking = draft.isParking
                this@CreateListingScreenModel.isFurnished = draft.isFurnished
                this@CreateListingScreenModel.isBalcony = draft.isBalcony
                this@CreateListingScreenModel.isCondition = draft.isCondition
                this@CreateListingScreenModel.gymIsSelect = draft.gymIsSelect
                this@CreateListingScreenModel.poolIsSelect = draft.poolIsSelect
                this@CreateListingScreenModel.elevatorIsSelect = draft.elevatorIsSelect
                this@CreateListingScreenModel.securityIsSelect = draft.securityIsSelect
                this@CreateListingScreenModel.verificationMethod = draft.verificationMethod

                // Розпаковуємо рядок фотографій назад у список
                this@CreateListingScreenModel.uploadedPhotos.clear()
                if(draft.photosUris.isNotBlank()){
                    this@CreateListingScreenModel.uploadedPhotos.addAll(draft.photosUris.split(","))
                }

                // Можна додати логіку визначення CurrentStep
                // На основі того, які дані вже заповнені
            }
        }
    }

    // Збереження чернетки (кнопка "Зберегти" в TopBar)
    fun saveDraft(onSuccess: () -> Unit){
        screenModelScope.launch {
            saveDraftInternal()

            onSuccess()
        }
    }

    // Внутрішня функція, яка гарантує що код виконається і дочекається результату
    private suspend fun saveDraftInternal(){
        // Якщо це нова чернетка, генеруємо ID. Якщо вже редагуємо - залишаємо старий.
        val currentDraftId = listingId ?: Random.nextInt(100000).toString()

        // Room не вміє автоматично зберігати списки (List<String>)
        // Тому конвертуємо список фото у рядок, розділений комами.
        val photosString = uploadedPhotos.joinToString(",")

        // Пакуємо всі наші стани в Entity
        val myListingEntity = MyListingEntity(
            localId = currentDraftId,
            listingStatus = ListingStatus.DRAFT,

            title = title,
            description = description,
            address = address,
            area = area,
            price = price,
            bedrooms = countBedrooms,
            bathrooms = countBathrooms,
            floorNumber = floorNumber,
            propertyType = selectedType,

            allowedPets = allowedPets,
            isParking = isParking,
            isFurnished = isFurnished,
            isBalcony = isBalcony,
            isCondition = isCondition,
            gymIsSelect = gymIsSelect,
            poolIsSelect = poolIsSelect,
            elevatorIsSelect = elevatorIsSelect,
            securityIsSelect = securityIsSelect,
            verificationMethod = verificationMethod,
            photosUris = photosString,

            likesNumber = 0,
            viewsNumber = 0,
            rejectsNumber = 0,
            goToContactsNumber = 0,
            createAt = TimeSource.Monotonic.markNow().toString(),
            updateAt = TimeSource.Monotonic.markNow().toString()
        )

        // Відправляємо в локальну базу даних
        dao.saveListing(myListingEntity)

        // Зберігаємо ID, щоб наступне натискання "Зберегти" оновило цей самий запис, а не створило новий
        listingId = currentDraftId
    }

    // Фінальна відправка на сервер
    @OptIn(ExperimentalTime::class)
    fun submitListing(onSuccess: () -> Unit) {
        // Якщо вже відправляється запит, повертаємось
        if (isLoading) return

        screenModelScope.launch {
            // Вмикаємо завантаження
            isLoading = true

            try {
                // Формуємо об'єкт для Ktor і робимо POST запит
                // Зберігаємо всі введені дані локально востаннє (щоб нічого не загубилось)
                saveDraftInternal()

                // Мапимо Enum типу нерухомості у строки для сервера
                val categoryString = when (selectedType) {
                    PropertyType.APARTMENT -> "APARTMENT"
                    PropertyType.HOUSE -> "HOUSE"
                    PropertyType.STUDIO -> "STUDIO"
                }

                //
                val parkingString = if (isParking) "YES" else "NO"

                //
                val request = CreatePropertyRequest(
                    title = title.ifBlank { "Нове оголошення" },
                    description = description.ifBlank { "Опис відсутній" },
                    localization = address,
                    price = price.toDoubleOrNull() ?: 0.0,
                    area = area.toDoubleOrNull() ?: 0.0,
                    rooms = countBedrooms,
                    category = categoryString,
                    subcategory = "RENT",
                    parking = parkingString,
                    petsAllowed = allowedPets,
                    elevator = elevatorIsSelect,
                    furniture = isFurnished,
                    buildingType = "APARTMENT",
                    status = "MODERATION"
                )

                /*TODO: Зробити токен у вигляді змінної*/
                val userToken = "d0d20eeb-2c54-4363-9b7b-0631ae0f9892"


                val isSuccess = withTimeoutOrNull(5.seconds) {
                    apiService.createProperty(request, userToken)
                }

                when (isSuccess) {
                    true -> {
                        // Отримуємо поточний
                        val currentId = listingId ?: return@launch

                        // Створюємо локальну аналітику, якщо сервер прийняв оголошення
                        val myListingEntity = MyListingEntity(
                            localId = currentId,
                            listingStatus = ListingStatus.MODERATION,

                            title = title,
                            description = description,
                            address = address,
                            area = area,
                            price = price,
                            bedrooms = countBedrooms,
                            bathrooms = countBathrooms,
                            floorNumber = floorNumber,
                            propertyType = selectedType,

                            allowedPets = allowedPets,
                            isParking = isParking,
                            isFurnished = isFurnished,
                            isBalcony = isBalcony,
                            isCondition = isCondition,
                            gymIsSelect = gymIsSelect,
                            poolIsSelect = poolIsSelect,
                            elevatorIsSelect = elevatorIsSelect,
                            securityIsSelect = securityIsSelect,
                            verificationMethod = verificationMethod,
                            photosUris = uploadedPhotos.joinToString(","),

                            likesNumber = 0,
                            viewsNumber = 0,
                            rejectsNumber = 0,
                            goToContactsNumber = 0,
                            createAt = Clock.System.now().toString(),
                            updateAt = Clock.System.now().toString()
                        )

                        dao.saveListing(myListingEntity)

                        println("Оголошення створено та відправлено на модерацію")
                        onSuccess()

                    }

                    false -> {
                        _snackbarEvent.send("Сервер відхилив запит. Перевірте дані")
                    }

                    else -> {
                        _snackbarEvent.send("Сервер не відповідає. Перевірте з'єднання та спробуйте ще раз")
                    }
                }
            } catch (e: Exception) {
                println("Помилка мережі: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                isLoading = false
            }
        }
    }
}