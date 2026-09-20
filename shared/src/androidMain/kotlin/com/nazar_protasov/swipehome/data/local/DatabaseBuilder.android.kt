package com.nazar_protasov.swipehome.data.local

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDatabase

lateinit var appContext: Context

actual fun getRoomDatabase(): SwipeHomeDatabase {
    return try {
        val dbFile = appContext.getDatabasePath("swipe_home.db")
        Room.databaseBuilder<SwipeHomeDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
            .fallbackToDestructiveMigration(true)
            .build()
    } catch (e: Throwable) {
        Log.e("RoomInit", "Failed to build Room database", e)
        throw e
    }
}
