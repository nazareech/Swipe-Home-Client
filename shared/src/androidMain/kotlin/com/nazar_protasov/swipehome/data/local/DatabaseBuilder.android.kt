package com.nazar_protasov.swipehome.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDatabase

lateinit var appContext: Context

actual fun getRoomDatabaseBuilder():  RoomDatabase.Builder<SwipeHomeDatabase> {
    val dbFile = appContext.getDatabasePath("swipe_home.db")
    return Room.databaseBuilder<SwipeHomeDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
