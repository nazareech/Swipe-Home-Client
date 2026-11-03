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

    // Прапорці для фонової пагінації (дозавантаження)
    private var isLoadingMore = false
    private var isLastPage = false

    private val userToken = sessionManager.tokenFlow.toString()

    init{
        // При створенні екрана одразу йдемо на сервер
        fetchProperties()
    }

    // Будемо викликати при старті, оновленні списку або після зміни фільтрів
    fun fetchProperties(){
        screenModelScope.launch {
            _isLoading.value = true
            isLastPage = false
            isLoadingMore = false

            // Скидаємо offset на 0 при повному оновленні
            val initialFilters = _currentFilters.value.copy(offset = 0)
            _currentFilters.value = initialFilters

            // Передаємо токен і поточні фільтри в API
            val result = apiService.fetchProperties(
                token = userToken,
                filterRequest = initialFilters
            )

            if (result.size < initialFilters.limit) {
                isLastPage = true
            }

            // Викликаємо Ktor клієнт
            _properties.value = result
            _isLoading.value = false
        }
    }

    // Фонове дозавантаження нових оголошень без показу повноекранного лоадера
    private fun loadMoreProperties() {
        if (isLoadingMore || isLastPage || _isLoading.value) return

        isLoadingMore = true
        screenModelScope.launch {
            val nextFilters = _currentFilters.value.copy(
                offset = _currentFilters.value.offset + _currentFilters.value.limit
            )
            _currentFilters.value = nextFilters

            val newProperties = apiService.fetchProperties(
                token = userToken,
                filterRequest = nextFilters
            )

            if (newProperties.size < nextFilters.limit) {
                isLastPage = true
            }

            if (newProperties.isNotEmpty()) {
                _properties.value = (_properties.value + newProperties).distinctBy { it.idProperty }
            }

            isLoadingMore = false
        }
    }

    // Функція для оновлення фільтрів з екрана фільтрації
    fun updateFilters(newFilters: FilterRequestDTO){
        _currentFilters.value = newFilters.copy(offset = 0)
        // Автоматично робимо новий запит на сервер після застосування фільтрів
        fetchProperties()
    }


    // Функція для видалення першої картинки зі списку після свайпу
    fun onCardSwiped(action: SwipeAction){
        val currentList = _properties.value

        if(currentList.isEmpty()) return

        // Беремо першу картку перед тим, як її видалити
        val swipedProperty = currentList.first()

        // Видаляємо картку зі списку на екрані
        val updatedList = currentList.drop(1)
        _properties.value = updatedList

        // У фоні відправляємо свайп на сервер
        screenModelScope.launch {
            val propertyIdInt = swipedProperty.idProperty?.toIntOrNull() ?: return@launch

            val request = SwipeRequestDTO(
                propertyIdInt,
                action.toString()
            )
            apiService.sendSwipe(
                token = userToken,
                infoDTO = request
            )
        }

        // Коли в стеку залишилось 3 або менше непереглянутих оголошень — підвантажуємо нові
        if (updatedList.size <= 3) {
            loadMoreProperties()
        }
    }
}