package com.nazar_protasov.swipehome.ui.screens.settings.listing

import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao

class MyListingsScreenModel(private val dao: SwipeHomeDao) {
    val listingsWithAnalytics = dao.getDraftsWithAnalytics()
}