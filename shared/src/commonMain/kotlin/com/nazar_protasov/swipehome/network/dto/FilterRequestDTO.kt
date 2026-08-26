package com.nazar_protasov.swipehome.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class FilterRequestDTO(
    val limit: Int = 10,
    val offset: Int = 0,

    // Базові фільтри
    val category: String? = null,       // APARTMENT, ROOM, HOUSE
    val subcategory: String? = null,    // RENT, SALE
    val localization: String? = null,

    // Діапазони
    val priceMin: Double? = null,
    val priceMax: Double? = null,
    val areaMin: Double? = null,
    val areaMax: Double? = null,
    val rooms: Int? = null,

    // Специфічні фільтри
    val floor: String? = null,
    val parking: String? = null,
    val buildingType: String? = null,
    val petsAllowed: Boolean? = null,
    val elevator: Boolean? = null,
    val furniture: Boolean? = null,
    val balcony: Boolean? = null
) {
    companion object {
        const val CATEGORY_APARTMENT = "APARTMENT"
        const val CATEGORY_HOUSE = "HOUSE"
        const val CATEGORY_ROOM = "ROOM"

        const val DEAL_RENT = "RENT"
        const val DEAL_SALE = "SALE"
    }
}
