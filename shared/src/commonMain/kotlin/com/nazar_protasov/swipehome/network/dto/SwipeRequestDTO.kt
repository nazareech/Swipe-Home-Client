package com.nazar_protasov.swipehome.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SwipeRequestDTO(
    val idProperty: Int,
    val action: String
)
