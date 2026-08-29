package com.nazar_protasov.swipehome.ui.models

import kotlinx.serialization.SerialName

data class Property(
    val id: String,
    val imagesUrl: List<String>,
    val price: String,
    val title: String,
    val location: String,
    val rooms: Int,
    val area: Double,
    val buildingType: String
)

data class PropertyDetails(
    val id_property: Int? = null,
    val id_owner: Int,
    val title: String,
    val description: String,
    val localization: String,
    val price: String,
    val area: String,
    val rooms: String,
    val category: String,
    @SerialName("subcategory") val subCategory: String,
    val parking: String,
    val pets_allowed: Boolean,
    val elevator: Boolean,
    val furniture: Boolean,
    val building_type: String,
    val status: String,
    val imagesUrl: List<String>? = null,
    val creates_at: String? = null
)