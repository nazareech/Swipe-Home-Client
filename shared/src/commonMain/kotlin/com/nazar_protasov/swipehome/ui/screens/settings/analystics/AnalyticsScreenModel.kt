package com.nazar_protasov.swipehome.ui.screens.settings.analystics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.entity.AnalyticsListingEntity
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
                val draft = data.draft
                val analytics = data.analytics

                this@AnalyticsScreenModel.listingId = draft.draftId
                this@AnalyticsScreenModel.title = draft.title
                this@AnalyticsScreenModel.countBedrooms = draft.bedrooms
                this@AnalyticsScreenModel.area = draft.area
                this@AnalyticsScreenModel.address = draft.address

                // Розпаковуємо рядок фотографій назад у список
                this@AnalyticsScreenModel.uploadedPhotos.clear()
                if (draft.photosUris.isNotBlank()){
                    this@AnalyticsScreenModel.uploadedPhotos.addAll(draft.photosUris.split(","))
                }

                this@AnalyticsScreenModel.likesNumber = analytics?.likesNumber ?: 0
                this@AnalyticsScreenModel.viewsNumber = analytics?.viewsNumber ?: 0
                this@AnalyticsScreenModel.rejectsNumber = analytics?.rejectsNumber ?: 0
                this@AnalyticsScreenModel.goToContactsNumber = analytics?.goToContactsNumber ?: 0
                this@AnalyticsScreenModel.listingStatus = analytics?.listingStatus ?: ListingStatus.DRAFT
            }
        }
    }

    val cards: Flow<List<AnalyticsListingEntity>>
        get() = dao.getAllMyCards()
}