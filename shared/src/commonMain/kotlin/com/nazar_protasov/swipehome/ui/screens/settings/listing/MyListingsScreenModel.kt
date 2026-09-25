package com.nazar_protasov.swipehome.ui.screens.settings.listing

import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDao

class MyListingsScreenModel(dao: SwipeHomeDao) {
    val myListingProperties = dao.getMyListing()
}