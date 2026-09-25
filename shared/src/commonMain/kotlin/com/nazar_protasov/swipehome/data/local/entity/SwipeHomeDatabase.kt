package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

@Database(
    entities = [
        DraftListingEntity::class,
        SwipeEntity::class,
        AnalyticsListingEntity::class
       ],
    version = 3,
    exportSchema = false
)
@TypeConverters(SwipeHomeTypeConverters::class)
@ConstructedBy(SwipeHomeDatabaseConstructor::class)
abstract class SwipeHomeDatabase : RoomDatabase() {
    abstract fun swipeHomeDao(): SwipeHomeDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object SwipeHomeDatabaseConstructor : RoomDatabaseConstructor<SwipeHomeDatabase> {
    override fun initialize(): SwipeHomeDatabase
}