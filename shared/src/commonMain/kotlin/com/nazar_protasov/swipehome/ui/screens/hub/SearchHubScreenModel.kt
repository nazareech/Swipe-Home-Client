package com.nazar_protasov.swipehome.ui.screens.hub

import cafe.adriel.voyager.core.model.ScreenModel
import com.nazar_protasov.swipehome.ui.models.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchHubScreenModel : ScreenModel {

    // 0 - Збережені, 1 - Відхтлені
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val _isCpmpareMode = MutableStateFlow(false)
    val isCompareMode: StateFlow<Boolean> = _isCpmpareMode.asStateFlow()

    // Зберігаємо ID вибраних об'єктів
    private val _selectedForCompare = MutableStateFlow<Set<String>>(emptySet())
    val selectedForCompare: StateFlow<Set<String>> = _selectedForCompare.asStateFlow()

    // TODO: Пізніше треба підключити до Ktor (PropertyApiService)
    private val _savedProperties = MutableStateFlow<List<Property>>(emptyList())
    val savedProperties: StateFlow<List<Property>> = _savedProperties.asStateFlow()

    private val _rejectedProperties = MutableStateFlow<List<Property>>(emptyList())
    val rejectedProperties: StateFlow<List<Property>> = _rejectedProperties.asStateFlow()

    init{
        // Тимчасові мокові дані для верстки
        _savedProperties.value = listOf(
            Property(id = "1", imagesUrl = listOf("https://images.unsplash.com/photo-1502672260266-1c1e52409818?q=80&w=1000"), price = "$1,200/міс", title = "Apartment", location = "New York", rooms = 2, area = 65.0, buildingType = "New", isNew = true),
            Property("2", imagesUrl = listOf("https://images.unsplash.com/photo-1502672260266-1c1e52409818?q=80&w=1000"), price = "$1,200/міс", title ="Apartment", location ="New York", rooms =2, area =45.0, buildingType = "Not New", isNew = false),
            Property("3", imagesUrl = listOf("https://images.unsplash.com/photo-1513694203232-719a280e022f?q=80&w=1000"), price = "$1,850/міс", title ="Loft", location ="Brooklyn", rooms =1, area =16.0, buildingType = "Grand", isNew = true),
            Property("4", imagesUrl = listOf("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?q=80&w=1000"), price = "$950/міс", title ="Studio", location ="Queens", rooms =1, area =24.0, buildingType = "Old", isNew = false),
            Property("5", imagesUrl = listOf("https://images.unsplash.com/photo-1502005097973-156150af5a85?q=80&w=1000"), price ="$2,100/міс", title = "Penthouse", location ="Manhattan", rooms = 3,  area = 120.0, buildingType = "New", isNew = true)
        )
        _rejectedProperties.value = listOf(
            Property(id = "6", imagesUrl = listOf("https://images.unsplash.com/photo-1502672260266-1c1e52409818?q=80&w=1000"), price = "$1,200/міс", title = "Apartment", location = "New York", rooms = 2, area = 65.0, buildingType = "New", isNew = true),
            Property("7", imagesUrl = listOf("https://images.unsplash.com/photo-1502672260266-1c1e52409818?q=80&w=1000"), price = "$1,200/міс", title ="Apartment", location ="New York", rooms =2, area =45.0, buildingType = "Not New", isNew = false),
            Property("8", imagesUrl = listOf("https://images.unsplash.com/photo-1513694203232-719a280e022f?q=80&w=1000"), price = "$1,850/міс", title ="Loft", location ="Brooklyn", rooms =1, area =16.0, buildingType = "Grand", isNew = true),
            Property("9", imagesUrl = listOf("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?q=80&w=1000"), price = "$950/міс", title ="Studio", location ="Queens", rooms =1, area =24.0, buildingType = "Old", isNew = false),
            Property("10", imagesUrl = listOf("https://images.unsplash.com/photo-1502005097973-156150af5a85?q=80&w=1000"), price ="$2,100/міс", title = "Penthouse", location ="Manhattan", rooms = 3,  area = 120.0, buildingType = "New", isNew = true),
            Property("11", imagesUrl = listOf("https://images.unsplash.com/photo-1502005097973-156150af5a85?q=80&w=1000"), price ="$2,100/міс", title = "Penthouse", location ="Manhattan", rooms = 3,  area = 120.0, buildingType = "New", isNew = true)
        )
    }

    fun setTabIndex(index: Int) {
        _selectedTabIndex.value = index
        _isCpmpareMode.value = false
        _selectedForCompare.value = emptySet()
    }

    fun toggleCompareMode(){
        _isCpmpareMode.value = !_isCpmpareMode.value
        if (!_isCpmpareMode.value) {
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