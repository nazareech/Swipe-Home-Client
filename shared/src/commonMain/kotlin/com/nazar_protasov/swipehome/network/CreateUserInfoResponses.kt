package com.nazar_protasov.swipehome.network

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserInfoResponses(
    val idUser: Int,
    val login: String,
    val firstName: String,
    val lastName: String,
    val aboutInfo: String,
    val email: String,
    val phone: String,
    val isVerifiedOwner: Boolean,
    val isAdmin: Boolean,
    val createdAt: String?= null,
    val lastSeen: String? = null,
    val avatarUrl: String? = null
)
