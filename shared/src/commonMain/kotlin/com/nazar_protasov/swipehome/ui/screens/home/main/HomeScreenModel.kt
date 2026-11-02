package com.nazar_protasov.swipehome.ui.screens.home.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.SessionManager
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.network.PropertyDTO
import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import com.nazar_protasov.swipehome.network.dto.SwipeRequestDTO
import com.nazar_protasov.swipehome.ui.utils.SwipeAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val apiService: PropertyApiService,
    private val sessionManager: SessionManager
) : ScreenModel {

    // Стан списку нерухомості
    private val _properties = MutableStateFlow<List<PropertyDTO>>(emptyList())
    val properties: StateFlow<List<PropertyDTO>> = _properties.asStateFlow()


    // Стан завантаження (щоб показувати крутилку)
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    // Стан поточних фільтрів (за замовчуванням - пусті)
    private val _currentFilters = MutableStateFlow(FilterRequestDTO())
    val currentFilters: StateFlow<FilterRequestDTO> = _currentFilters.asStateFlow()

    // TODO: Отримати реальний токен з сервера
    private val fakeUserToken = "4002ad1c-513b-4b51-85b1-851e21ea202d"

    init{
        // При створенні екрана одразу йдемо на сервер
        fetchProperties()
    }

    // Будемо викликати після оновлення фільтрів
    fun fetchProperties(){
        screenModelScope.launch {
            _isLoading.value = true

            // Передаємо токен і поточні фільтри в API
            val result = apiService.fetchProperties(
                token = fakeUserToken,
                filterRequest = _currentFilters.value
            )

            // Викликаємо Ktor клієнт
            _properties.value = result
            _isLoading.value = false
        }
    }

    // Функція для оновлення фільтрів з екрана фільтрації
    fun updateFilters(newFilters: FilterRequestDTO){
        _currentFilters.value = newFilters
        // Автоматично робимо новий запит на сервер після застосування фільтрів
        fetchProperties()
    }


    // Функція для видалення першої картинки зі списку після свайпу
    fun onCardSwiped(action: SwipeAction){
        val currentList = _properties.value

        if(currentList.isEmpty()) return

        if (currentList.isNotEmpty()){
            // Беремо першу картку перед тим, як її видалити
            val swipedProperty = currentList.first()

            // Видаляємо картку зі списку на екрані
            _properties.value = currentList.drop(1)

            // У фоні відправляємо свайп на сервер
            screenModelScope.launch {
                val propertyIdInt = swipedProperty.idProperty?.toIntOrNull() ?: return@launch

                val token = sessionManager.tokenFlow.firstOrNull() ?: fakeUserToken

                val request = SwipeRequestDTO(
                    propertyIdInt,
                    action.toString()
                )
                apiService.sendSwipe(
                    token = token,
                    infoDTO = request
                )
            }

            // Коли карток стає мало (наприклад менше ніж 2), можна додавати логіку збільшення
            // offset та дозавантаження (пагінації)
        }
    }
}