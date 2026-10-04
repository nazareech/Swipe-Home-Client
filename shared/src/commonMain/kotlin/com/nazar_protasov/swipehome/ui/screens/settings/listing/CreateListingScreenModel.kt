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
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.ParkingType
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
    var serverId by mutableStateOf<String?>(null)

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
    var parkingType by mutableStateOf(ParkingType.NONE)
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
                this@CreateListingScreenModel.serverId = draft.serverId
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
                this@CreateListingScreenModel.parkingType = draft.parkingType
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
                    val photos = draft.photosUris.split(",").map { path ->
                        if (path.startsWith("http") || path.startsWith("file://")) path
                        else "${PropertyApiService.BASE_URL}/uploads/$path"
                    }
                    this@CreateListingScreenModel.uploadedPhotos.addAll(photos)
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
            serverId = this@CreateListingScreenModel.serverId,
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
            parkingType = parkingType,
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
                val currentId = listingId ?: Random.nextInt(100000).toString()
                listingId = currentId

                /*TODO: Зробити токен у вигляді змінної*/
                val userToken = "d0d20eeb-2c54-4363-9b7b-0631ae0f9892"

                // Зберігаємо всі введені дані локально востаннє (щоб нічого не загубилось)
                saveDraftInternal()

                // Відділяємо старі (вже на сервері) фотографії
                val existingPhotosOnServer = uploadedPhotos.filter { it.startsWith("http") }
                    .map { it.substringAfter("/uploads/") } // Передаємо бекенду тільки відносні шляхи

                // Готуємо дані для відправки на сервер
                val request = CreatePropertyRequest(
                    idProperty = this@CreateListingScreenModel.serverId,
                    title = title.ifBlank { "Нове оголошення" },
                    description = description.ifBlank { "Опис відсутній" },
                    photosList = existingPhotosOnServer,
                    localization = address,
                    price = price.toDoubleOrNull() ?: 0.0,
                    area = area.toDoubleOrNull() ?: 0.0,
                    rooms = countBedrooms,
                    category = selectedType.name,
                    subcategory = "RENT",
                    parkingType = parkingType.code,
                    petsAllowed = allowedPets,
                    elevator = elevatorIsSelect,
                    furniture = isFurnished,
                    buildingType = "APARTMENT",
                    status = "MODERATION"
                )

                println("ВІДПРАВКА НА СЕРВЕР: idProperty = ${request.idProperty}")

                // Відправляємо Оголошення (Отримуємо об'єкт відповіді від сервера)
                val serverResponse = withTimeoutOrNull(30.seconds) {
                    apiService.createProperty(request, uploadedPhotos, userToken)
                }

                if (serverResponse?.idProperty != null) {
                    // Отримуємо поточний
                    val currentId = listingId ?: return@launch

                    this@CreateListingScreenModel.serverId = serverResponse.idProperty
                    // Створюємо локальну аналітику, якщо сервер прийняв оголошення
                    val myListingEntity = MyListingEntity(
                        localId = currentId,
                        serverId = serverResponse.idProperty, // записуємо той самий ID, що отримали з сервера
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
                        parkingType = parkingType,
                        isFurnished = isFurnished,
                        isBalcony = isBalcony,
                        isCondition = isCondition,
                        gymIsSelect = gymIsSelect,
                        poolIsSelect = poolIsSelect,
                        elevatorIsSelect = elevatorIsSelect,
                        securityIsSelect = securityIsSelect,
                        verificationMethod = verificationMethod,
                        
                        // Записуємо нові шляхи від сервера, якщо вони є. Інакше залишаємо локальні
                        photosUris = if (serverResponse.images.isNotEmpty()) {
                            serverResponse.images.joinToString(",") { "${PropertyApiService.BASE_URL}/uploads/$it" }
                        } else {
                            uploadedPhotos.joinToString(",")
                        },

                        likesNumber = 0,
                        viewsNumber = 0,
                        rejectsNumber = 0,
                        goToContactsNumber = 0,
                        createAt = Clock.System.now().toString(),
                        updateAt = Clock.System.now().toString()
                    )
                    dao.saveListing(myListingEntity)

                    println(serverResponse.message)
                    println(serverResponse.images)
                    _snackbarEvent.send(serverResponse.message)
                    onSuccess()

                } else {
                    _snackbarEvent.send("Сервер не відповідає або сталася помилка. Спробуйте ще раз.")
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