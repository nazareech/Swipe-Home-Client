package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DraftListingEntity::class, SwipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SwipeHomeTypeConverters::class)
abstract class SwipeHomeDatabase : RoomDatabase() {
    abstract fun swipeHomeDao(): SwipeHomeDao
}