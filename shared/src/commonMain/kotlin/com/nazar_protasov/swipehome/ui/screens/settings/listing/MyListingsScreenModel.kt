package com.nazar_protasov.swipehome.ui.screens.settings.listing

import com.nazar_protasov.swipehome.data.local.entity.DraftListingEntity
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao
import kotlinx.coroutines.flow.Flow

class MyListingsScreenModel(private val dao: SwipeHomeDao) {
    val drafts: Flow<List<DraftListingEntity>>
        get() = dao.getAllDrafts()
}