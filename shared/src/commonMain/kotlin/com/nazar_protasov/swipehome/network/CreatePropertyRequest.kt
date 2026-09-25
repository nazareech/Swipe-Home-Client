package com.nazar_protasov.swipehome.network

import kotlinx.serialization.Serializable

@Serializable
data class CreatePropertyRequest(
    val title: String,
    val description: String,
    val localization: String,
    val price: Double,
    val area: Double,
    val rooms: Int,
    val category: String,
    val subcategory: String,
    val parking: String,
    val pets_allowed: Boolean,
    val elevator: Boolean,
    val furniture: Boolean,
    val building_type: String,
    val status: String
)
