package com.nazar_protasov.swipehome.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun getRoomDatabaseBuilder(): RoomDatabase.Builder<SwipeHomeDatabase> {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dbFilePath = (documentDirectory?.path) + "/swipe_home.db"
    return Room.databaseBuilder<SwipeHomeDatabase>(
        name = dbFilePath
    )
}
