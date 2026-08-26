package com.nazar_protasov.swipehome.ui.screens.home

import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.models.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenModel(private val apiService: PropertyApiService) : ScreenModel {

    // Стан списку нерухомості
    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> = _properties.asStateFlow()


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
    fun onCardSwiped(){
        val currentList = _properties.value
        if (currentList.isNotEmpty()){
            _properties.value = currentList.drop(1)
            // Коли карток стає мало (наприклад менше ніж 2), можна додавати логіку збільшення
            // offset та дозавантаження (пагінації)
        }
    }
}