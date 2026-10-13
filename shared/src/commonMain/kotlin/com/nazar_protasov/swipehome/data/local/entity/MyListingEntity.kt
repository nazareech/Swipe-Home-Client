package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.ParkingType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.PropertyType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.VerificationMethods
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName="my_listings")
data class MyListingEntity(
    @PrimaryKey
    val localId: String,        // Локальний ID
    val serverId: String? = null,  // ID з сервера

    val listingStatus: ListingStatus,

    val title: String,
    val description: String,
    val address: String,
    val area: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val price: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val floorNumber: Int,
    val propertyType: PropertyType,

    val allowedPets: Boolean,
    val parkingType: ParkingType,
    val isFurnished: Boolean,
    val isBalcony: Boolean,
    val isCondition: Boolean,
    val gymIsSelect: Boolean,
    val poolIsSelect: Boolean,
    val elevatorIsSelect: Boolean,
    val securityIsSelect: Boolean,
    val verificationMethod: VerificationMethods,
    // Зберігаємо список шляхів до фотографій як єдиний рядок розділених символами ","
    val photosUris: String,

    // Поля аналітики (для чернеток тут нулі)
    val likesNumber: Int = 0,
    val viewsNumber: Int = 0,
    val rejectsNumber: Int = 0,
    val goToContactsNumber: Int = 0,
    val createAt: String,
    val updateAt: String // Час останнього оновлення
)
