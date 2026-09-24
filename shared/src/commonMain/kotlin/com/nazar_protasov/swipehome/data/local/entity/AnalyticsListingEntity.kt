package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus

@Entity(tableName = "analytics_listing")
data class AnalyticsListingEntity(
    @PrimaryKey
    val analyticId: String,
    val title: String,
    val description: String,
    val likesNumber: Int,
    val viewsNumber: Int,
    val rejectsNumber: Int,
    val goToContactsNumber: Int,
    val listingStatus: ListingStatus,
    val area: String,
    val bedrooms: Int,
    val address: String,
    val imageUrl: String
)