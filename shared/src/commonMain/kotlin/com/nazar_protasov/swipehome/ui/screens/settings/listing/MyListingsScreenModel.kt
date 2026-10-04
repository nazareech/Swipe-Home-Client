package com.nazar_protasov.swipehome.ui.screens.settings.listing

import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MyListingsScreenModel(private val apiService: PropertyApiService, private val dao: SwipeHomeDao) : ScreenModel {
    val myListingProperties = dao.getMyListing()

    private val _properties = MutableStateFlow<List<PropertyDTO>>(emptyList())
    val uploadedPhotos = mutableStateListOf<String>()

    // Стан завантаження (щоб показувати крутилку)
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // TODO: Отримати реальний токен з сервера
    private val fakeUserToken = "d0d20eeb-2c54-4363-9b7b-0631ae0f9892"

    init{
        // При створенні екрана одразу йдемо на сервер
        fetchOwnProperties()
    }

    fun fetchOwnProperties(){
        screenModelScope.launch {
            _isLoading.value = true

            // Передаємо токен і поточні фільтри в API
            val result = apiService.fetchOwnProperties(
                token = fakeUserToken
            )

            println(result)

            if(result.isNotEmpty()){
                result.forEach { dto ->
                    // Якщо це нова чернетка, генеруємо ID. Якщо вже редагуємо - залишаємо старий.
                    val currentDraftId = dto.idProperty ?: Random.nextInt(100000).toString()
                    // Room не вміє автоматично зберігати списки (List<String>)
                    // Тому конвертуємо список фото у рядок, розділений комами.
                    val photosString = dto.imagesMap?.keys?.joinToString(",") ?: ""

                    val currentProperty = MyListingEntity(
                        localId = currentDraftId ,
                        serverId = dto.idProperty,

                        // Безпечний мапінг статусу (якщо прийде "active", воно переведе його у верхній регістр)
                        listingStatus = try { ListingStatus.valueOf(dto.status.uppercase()) } catch (e: Exception) { ListingStatus.DRAFT },

                        title = dto.title,
                        description = dto.description,
                        address = dto.localization,
                        area = dto.area.toString(),
                        price = dto.price.toString(),
                        bedrooms = dto.rooms,
                        bathrooms = 1,
                        floorNumber = 1,
                        // Безпечний мапінг категорії нерухомості
                        propertyType = try { PropertyType.valueOf(dto.category.uppercase()) } catch (e: Exception) { PropertyType.APARTMENT },

                        allowedPets = dto.petsAllowed,
                        // Безпечний мапінг типу паркінгу
                        parkingType = try { ParkingType.valueOf(dto.parking.uppercase()) } catch (e: Exception) { ParkingType.NONE },
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
    }
}