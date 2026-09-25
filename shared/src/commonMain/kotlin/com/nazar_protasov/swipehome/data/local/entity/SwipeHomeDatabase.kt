@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

@Database(
    entities = [
        MyListingEntity::class,
        SwipeEntity::class
       ],
    version = 4,
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