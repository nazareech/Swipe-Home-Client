package com.nazar_protasov.swipehome.ui.screens.hub

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.SessionManager
import com.nazar_protasov.swipehome.data.local.entity.HistoryPropertyEntity
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao
import com.nazar_protasov.swipehome.data.local.entity.toEntity
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.models.Property
import com.nazar_protasov.swipehome.ui.screens.hub.components.toUIProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DateFilter { ALL, NEWEST }
class SearchHubScreenModel(
    private val apiService: PropertyApiService,
    private val dao: SwipeHomeDao,
    private val sessionManager: SessionManager
) : ScreenModel {

    // 0 - Збережені (RIGHT), 1 - Відхилені (LEFT)
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    // ФІльтр за датою: "Всі" чи "Нові"
    private val _selectedDateFilter = MutableStateFlow(DateFilter.ALL)
    val selectedDateFilter: StateFlow<DateFilter> = _selectedDateFilter.asStateFlow()


    // Реактивні списки з бази
    val savedProperties: StateFlow<List<Property>> = combine(
        dao.getSwipeHistoryByAction("RIGHT"),
        _selectedDateFilter
    ) { entities, filter ->
        applyFilter(entities, filter).map { it.toUIProperty() }
    }.stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rejectedProperties: StateFlow<List<Property>> = combine(
        dao.getSwipeHistoryByAction("LEFT"),
        _selectedDateFilter
    ) { entities, filter ->
        applyFilter(entities, filter).map { it.toUIProperty() }
    }.stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
//        screenModelScope.launch {
//            sessionManager.saveToken("c88b4054-e1d9-4c3a-9f05-3ea64b223ee7")
//        }
        refreshHistory()
    }

    fun refreshHistory() {
        screenModelScope.launch {
            // Отримуємо токен користувача
            val token = sessionManager.tokenFlow.firstOrNull() ?: return@launch
            println("Отримано токен: $token")
            // Завантажуємо з сервера
            val dtoList = apiService.fetchSwipeHistory(token)

            println("Отримано ${dtoList} з сервера")
            if (dtoList.isNotEmpty()) {
                // Зберігаємо в Room (UI оновиться сам через Flow)
                dao.insertSwipeHistory(dtoList.map {it.toEntity()})
            }
        }
    }

    fun setDataFilter(filter: DateFilter) {
        _selectedDateFilter.value = filter
    }
    private fun applyFilter(
        items: List<HistoryPropertyEntity>,
        filter: DateFilter
    ): List<HistoryPropertyEntity> {
        return when (filter) {
            DateFilter.ALL -> items.sortedByDescending{ it.createsAt }
            DateFilter.NEWEST -> {
                // ФІльтруємо останні додані
                items.sortedByDescending { it.createsAt }
                // Або можна додати перевірку за останні 7-30 днів
            }
        }
    }

    private val _isCompareMode = MutableStateFlow(false)
    val isCompareMode: StateFlow<Boolean> = _isCompareMode.asStateFlow()

    // Зберігаємо ID вибраних об'єктів
    private val _selectedForCompare = MutableStateFlow<Set<String>>(emptySet())
    val selectedForCompare: StateFlow<Set<String>> = _selectedForCompare.asStateFlow()

    fun setTabIndex(index: Int) {
        _selectedTabIndex.value = index
        _isCompareMode.value = false
        _selectedForCompare.value = emptySet()
    }

    fun toggleCompareMode(){
        _isCompareMode.value = !_isCompareMode.value
        if (!_isCompareMode.value) {
            _selectedForCompare.value = emptySet()
        }
    }

    fun togglePropertySelection(propertyId: String) {
        _selectedForCompare.update { currentSet ->
            if (currentSet.contains(propertyId)) {
                currentSet - propertyId
            } else {
                currentSet + propertyId
            }
        }
    }
}