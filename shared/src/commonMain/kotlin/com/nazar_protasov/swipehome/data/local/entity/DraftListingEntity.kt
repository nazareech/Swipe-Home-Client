package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.PropertyType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.VerificationMethods
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName="draft_listings")
data class DraftListingEntity(
    @PrimaryKey
    val draftId: String,
    val propertyType: PropertyType,
    val address: String,
    val area: String,
    val price: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val title: String,
    val description: String,

    val floorNumber: Int,
    val allowedPets: Boolean,
    val isParking: Boolean,
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
    val updateAt: String // Час останнього оновлення
)
