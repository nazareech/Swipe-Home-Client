package com.nazar_protasov.swipehome.ui.screens.home.main

import cafe.adriel.voyager.core.model.ScreenModel
import com.nazar_protasov.swipehome.data.local.SessionManager
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.network.PropertyDTO
import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import com.nazar_protasov.swipehome.network.dto.SwipeRequestDTO
import com.nazar_protasov.swipehome.ui.utils.SwipeAction
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenModel(
    private val apiService: PropertyApiService,
    private val sessionManager: SessionManager
) : ScreenModel {

    // Власний CoroutineScope синглтон, який не скасовує Voyager при закритті екранів
    private val coroutineScore = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

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
    private var fetchJob: Job? = null
    private var isLoadingMore = false
    private var isLastPage = false

    init{
        // При створенні екрана одразу йдемо на сервер
        fetchProperties()
    }

    // Будемо викликати при старті, оновленні списку або після зміни фільтрів
    fun fetchProperties(){
        // скасовуємо попередній запит, якщо він ще триває
        fetchJob?.cancel()

        fetchJob = coroutineScore.launch {
            _isLoading.value = true
            isLastPage = false
            isLoadingMore = false

            try {
                // Скидаємо offset на 0 при повному оновленні
                val initialFilters = _currentFilters.value.copy(offset = 0)
                _currentFilters.value = initialFilters

                println("Встановлені для передачі фільтри fetchProperties: \n$initialFilters")

                val userToken = sessionManager.tokenFlow.firstOrNull() ?: return@launch

                // Передаємо токен і поточні фільтри в API
                // Огортаємо мережевий запит у NonCancellable,
                // щоб navigator.pop() не міг перервати отримання даних з сервера!
                val result = withContext(NonCancellable) {
                    apiService.fetchProperties(
                        token = userToken,
                        filterRequest = initialFilters
                    )
                }

                println("Отримано карток від сервера: ${result.size}")

                if (result.size < initialFilters.limit) {
                    isLastPage = true
                }

                // Викликаємо Ktor клієнт
                _properties.value = result
            } catch (e: CancellationException){
                println("fetchProperties скасовано через CancellationException")
                throw e
            } catch (e: Exception) {
                println("Помилка під час завантаження картинок: ${e.message}")
                e.printStackTrace()
            } finally {
                println("Ми дійшли до закінчення завантаження")
                _isLoading.value = false
            }
        }
    }

    // Фонове дозавантаження нових оголошень без показу повноекранного завантаження
    private fun loadMoreProperties() {
        if (isLoadingMore || isLastPage || _isLoading.value) return

        isLoadingMore = true
        coroutineScore.launch {
            val nextFilters = _currentFilters.value.copy(
                offset = _currentFilters.value.offset + _currentFilters.value.limit
            )
            _currentFilters.value = nextFilters

            val userToken = sessionManager.tokenFlow.firstOrNull() ?: return@launch

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
        println("Встановлені для передачі фільтри updateFilters: \n$newFilters")
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
        coroutineScore.launch {
            val propertyIdInt = swipedProperty.idProperty?.toIntOrNull() ?: return@launch

            val userToken = sessionManager.tokenFlow.firstOrNull() ?: return@launch
            
            val request = SwipeRequestDTO(
                propertyIdInt,
                action.toString()
            )
            apiService.sendSwipe(
                token = userToken,
                infoDTO = request
            )
        }

        // Коли в стеку залишилось 3 або менше не переглянутих оголошень підвантажуємо нові
        if (updatedList.size <= 3) {
            loadMoreProperties()
        }
    }
}