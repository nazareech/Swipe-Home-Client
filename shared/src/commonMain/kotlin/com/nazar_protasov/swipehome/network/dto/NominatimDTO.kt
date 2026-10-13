package com.nazar_protasov.swipehome.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NominatimResponse(
    @SerialName("place_id")
    val placeId: Long = 0,
    val lat: String,
    val lon: String,
    @SerialName("display_name")
    val displayName: String,
    val address: AddressDetails? = null
)

@Serializable
class AddressDetails {
    val road: String? = null
    @SerialName("house_number")
    val houseNumber: String? = null
    val city: String? = null
    val town: String? = null
    val country: String? = null
}
