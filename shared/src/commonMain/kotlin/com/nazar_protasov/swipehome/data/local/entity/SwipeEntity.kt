package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nazar_protasov.swipehome.ui.utils.SwipeAction

@Entity(tableName = "swipes_history")
data class SwipeEntity(
    @PrimaryKey
    val swipeId: String,
    val userId: String,
    val propertyId: String,
    val action: SwipeAction,
    val timestamp: Long,
    val createAt: Long,
    val isSynced: Boolean
)
