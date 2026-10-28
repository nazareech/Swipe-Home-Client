package com.nazar_protasov.swipehome.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SwipeHistoryResponseDTO(
    val history: List<SwipeHistoryItemDTO>
)

@Serializable
data class SwipeHistoryItemDTO(
    val idProperty: String,
    val action: String, // "RIGHT" або "LEFT"
    val idOwner: String,
    val title: String,
    val description: String,
    val localization: String,
    val latitude: Double,
    val longitude: Double,
    val price: Double,
    val area: Double,
    val rooms: Int,
    val category: String,
    val subcategory: String,
    val parking: String,
    val petsAllowed: Boolean,
    val elevator: Boolean,
    val furniture: Boolean,
    val buildingType: String,
    val status: String,
    val imagesMap: List<String> = emptyList(),
    val mainImage: String? = null,
    val createsAt: String // "2026-09-29T19:34:01.899466Z"
)