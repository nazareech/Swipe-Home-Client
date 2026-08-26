package com.nazar_protasov.swipehome.ui.models

data class Property(
    val id: String,
    val imageUrl: String,
    val price: String,
    val title: String,
    val location: String,
    val rooms: Int,
    val area: Double,
    val buildingType: String
)