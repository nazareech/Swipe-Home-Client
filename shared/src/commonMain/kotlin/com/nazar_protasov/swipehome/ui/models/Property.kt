package com.nazar_protasov.swipehome.ui.models

data class Property(
    val id: String,
    val imageUrl: String,
    val price: String,
    val title: String,
    val location: String,
    val details: String // Наприклад: "3 кімнати • 85 м² • 5 поверх"
)