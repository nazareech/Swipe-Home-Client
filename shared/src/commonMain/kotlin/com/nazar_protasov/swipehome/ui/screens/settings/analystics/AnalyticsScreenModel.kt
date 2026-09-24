package com.nazar_protasov.swipehome.ui.screens.settings.analystics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class AnalyticsScreenModel(
    private val dao: SwipeHomeDao
): ScreenModel {
    var listingId by mutableStateOf<String?>(null)

    var title by mutableStateOf("")

    val uploadedPhotos = mutableStateListOf<String>()
    var address by mutableStateOf("")
    var countBedrooms by mutableStateOf(0)
    var area by mutableStateOf("")

    var viewsNumber by mutableStateOf(0)
    var likesNumber by mutableStateOf(0)
    var rejectsNumber by mutableStateOf(0)
    var goToContactsNumber by mutableStateOf(0)
    var listingStatus by mutableStateOf(ListingStatus.ACTIVE)

    fun loadCard(cardId: String){
        screenModelScope.launch(Dispatchers.IO) {
            val card = dao.getCardById(cardId)

            if(card != null){
                this@AnalyticsScreenModel.listingId = card.analyticId
                this@AnalyticsScreenModel.title = card.title
                this@AnalyticsScreenModel.likesNumber = card.likesNumber
                this@AnalyticsScreenModel.viewsNumber = card.viewsNumber
                this@AnalyticsScreenModel.rejectsNumber = card.rejectsNumber
                this@AnalyticsScreenModel.goToContactsNumber = card.goToContactsNumber
                this@AnalyticsScreenModel.listingStatus = card.listingStatus
                this@AnalyticsScreenModel.countBedrooms = card.bedrooms
                this@AnalyticsScreenModel.area = card.area
                this@AnalyticsScreenModel.address = card.address

                // Розпаковуємо рядок фотографій назад у список
                this@AnalyticsScreenModel.uploadedPhotos.clear()
                if (card.imageUrl.isNotBlank()){
                    this@AnalyticsScreenModel.uploadedPhotos.addAll(card.imageUrl.split(","))
                }
            }
        }
    }
}