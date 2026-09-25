package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.TypeConverter
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.PropertyType
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components.VerificationMethods
import com.nazar_protasov.swipehome.ui.utils.SwipeAction

class SwipeHomeTypeConverters {

    @TypeConverter
    fun fromPropertyType(value: PropertyType?): String? = value?.name

    @TypeConverter
    fun toPropertyType(value: String?): PropertyType? = value?.let {
        runCatching { PropertyType.valueOf(it) }.getOrDefault(PropertyType.APARTMENT)
    }

    @TypeConverter
    fun fromVerificationMethod(value: VerificationMethods?): String? = value?.name

    @TypeConverter
    fun toVerificationMethod(value: String?): VerificationMethods? = value?.let {
        runCatching { VerificationMethods.valueOf(it) }.getOrDefault(VerificationMethods.DIIA)
    }

    @TypeConverter
    fun fromSwipeAction(value: SwipeAction?): String? = value?.name

    @TypeConverter
    fun toSwipeAction(value: String?): SwipeAction? = value?.let {
        runCatching { SwipeAction.valueOf(it) }.getOrDefault(SwipeAction.RIGHT)
    }

    @TypeConverter
    fun fromListingStatus(value: ListingStatus?): String? = value?.name

    @TypeConverter
    fun toListingStatus(value: String?): ListingStatus? = value?.let {
        runCatching { ListingStatus.valueOf(it) }.getOrDefault(ListingStatus.DRAFT)
    }
}
