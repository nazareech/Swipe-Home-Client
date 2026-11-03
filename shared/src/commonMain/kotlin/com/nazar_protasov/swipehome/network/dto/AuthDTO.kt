package com.nazar_protasov.swipehome.network.dto

import kotlinx.serialization.SerialName
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

@Serializable
data class ForgotPasswordRequestDTO(
    val emailOrLogin: String
)

@Serializable
data class VerifyCodeRequestDTO(
    val emailOrLogin: String,
    val code: String
)

@Serializable
data class ResetPasswordRequestDTO(
    val emailOrLogin: String,
    val code: Int,
    val newPassword: String
)

@Serializable
data class MessageResponseDTO(
    val message: String
)
