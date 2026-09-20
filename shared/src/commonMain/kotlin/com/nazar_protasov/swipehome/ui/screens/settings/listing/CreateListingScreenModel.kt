package com.nazar_protasov.swipehome.ui.screens.settings.listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.PropertyType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.VerificationMethods
import kotlinx.coroutines.launch

class CreateListingScreenModel(
//    private val apiService: ListingsApiService
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
    var gymIsSelect by mutableStateOf(false)
    var poolIsSelect by mutableStateOf(false)
    var elevatorIsSelect by mutableStateOf(false)
    var securityIsSelect by mutableStateOf(false)

    val uploadedPhotos = mutableStateListOf<String>()

    var verificationMethod by mutableStateOf(VerificationMethods.DIIA)

    // Завантаження існуючої чернетки
    fun loadDraft(draftId: String) {
        screenModelScope.launch {
            listingId = draftId
            // TODO: Зробити Get запит за draftId та заповнити поля
            // val draft = apiService.getDraft(draftId)
            // this@CreateListingScreenModel.address = draft.address...
        }
    }

    // Збереження чернетки (кнопка "Зберегти" в TopBar)
    fun saveDraft(onSuccess: () -> Unit){
        screenModelScope.launch {
            // TODO: POST або PUT на ендпоінт чернеток
            // apiService.saveDraft(listingId, address, area, price, ...)
            println("Чернетку збережено")
            onSuccess()
        }
    }

    // Фінальна відправка на сервер
    fun submitListing(onSuccess: () -> Unit){
        screenModelScope.launch {
            // Формуємо об'єкт для Ktor і робимо POST запит
            /*
            val request = CreateListingRequest(
                address = address,
                area = area,
                price = price.toDoubleOrNull() ?: 0.0,
                ...
            )
            val result = apiService.postListing(request)
            */
            println("Оголошення створено та відправлено на модерацію")
            onSuccess()
        }
    }
}