package com.nazar_protasov.swipehome.network

import kotlinx.serialization.Serializable

@Serializable
data class CreatePropertyRequest(
    val idProperty: String? = null,
    val title: String,
    val description: String,
    val photosList: List<String>,
    val localization: String,
    val price: Double,
    val area: Double,
    val rooms: Int,
    val category: String,
    val subcategory: String,
    val parkingType: String,
    val petsAllowed: Boolean,
    val elevator: Boolean,
    val furniture: Boolean,
    val buildingType: String,
    val status: String
)
