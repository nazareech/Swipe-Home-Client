package com.nazar_protasov.swipehome.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.nazar_protasov.swipehome.data.local.entity.AnalyticsListingEntity
import com.nazar_protasov.swipehome.data.local.entity.DraftListingEntity

data class ListingWithAnalytics(
    @Embedded
    val draft: DraftListingEntity,

    @Relation(
        // Колонки по яких шукаємо збіг
        parentColumn = "draftId",
        entityColumn = "analyticId"
    )
    val analytics: AnalyticsListingEntity?
)
