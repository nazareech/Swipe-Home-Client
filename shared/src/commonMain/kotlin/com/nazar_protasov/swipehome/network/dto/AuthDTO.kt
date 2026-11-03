package com.nazar_protasov.swipehome.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDTO(
    val login: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val isVerifiedOwner: Boolean,
    val isAdmin: Boolean
)

@Serializable
data class AuthResponseDTO(
    val token: String
)
