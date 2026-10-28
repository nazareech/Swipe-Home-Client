package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nazar_protasov.swipehome.network.dto.SwipeHistoryItemDTO

@Entity(tableName = "swipe_history_properties")
data class HistoryPropertyEntity(
    @PrimaryKey
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
    val mainImage: String? = null,
    val imagesList: String, // Список URL-адрес зображень, розділені комами
    val createsAt: String // Дата виставлення
)

fun SwipeHistoryItemDTO.toEntity(): HistoryPropertyEntity {
    return HistoryPropertyEntity(
        idProperty = this.idProperty,
        action = this.action,
        idOwner = this.idOwner,
        title = this.title,
        description = this.description,
        localization = this.localization,
        latitude = this.latitude,
        longitude = this.longitude,
        price = this.price,
        area = this.area,
        rooms = this.rooms,
        category = this.category,
        subcategory = this.subcategory,
        parking = this.parking,
        petsAllowed = this.petsAllowed,
        elevator = this.elevator,
        furniture = this.furniture,
        buildingType = this.buildingType,
        status = this.status,
        mainImage = this.mainImage,
        imagesList = this.imagesMap.joinToString(","),
        createsAt = this.createsAt
    )
}
