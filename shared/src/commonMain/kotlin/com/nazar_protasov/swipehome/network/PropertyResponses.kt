package com.nazar_protasov.swipehome.network

import kotlinx.serialization.Serializable

// Модель відповіді при створенні
@Serializable
data class CreatePropertyResponse(
    val message: String,
    val idProperty: String? = null,
    val images: List<String> = emptyList()
)

// Модель відповіді при завантаженні фото
@Serializable
data class UploadImagesResponse(
    val message: String,
    val images: List<String>
)


