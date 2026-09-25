package com.nazar_protasov.swipehome.ui.screens.settings.analystics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.entity.MyListingEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

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
            val data = dao.getListWithAnalyticsById(cardId)

            if(data != null){

                this@AnalyticsScreenModel.listingId = data.localId
                this@AnalyticsScreenModel.title = data.title
                this@AnalyticsScreenModel.countBedrooms = data.bedrooms
                this@AnalyticsScreenModel.area = data.area
                this@AnalyticsScreenModel.address = data.address

                // Розпаковуємо рядок фотографій назад у список
                this@AnalyticsScreenModel.uploadedPhotos.clear()
                if (data.photosUris.isNotBlank()){
                    this@AnalyticsScreenModel.uploadedPhotos.addAll(data.photosUris.split(","))
                }

                this@AnalyticsScreenModel.likesNumber = data.likesNumber
                this@AnalyticsScreenModel.viewsNumber = data.viewsNumber
                this@AnalyticsScreenModel.rejectsNumber = data.rejectsNumber
                this@AnalyticsScreenModel.goToContactsNumber = data.goToContactsNumber
                this@AnalyticsScreenModel.listingStatus = data.listingStatus
            }
        }
    }

    val cards: Flow<List<MyListingEntity>>
        get() = dao.getAllMyCards()
}