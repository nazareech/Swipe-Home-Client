package com.nazar_protasov.swipehome.data.local

import androidx.room.Room
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun getRoomDatabase(): SwipeHomeDatabase {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dbFilePath = (documentDirectory?.path ?: "") + "/swipe_home.db"
    return Room.databaseBuilder<SwipeHomeDatabase>(
        name = dbFilePath,
        factory = { SwipeHomeDatabase::class.instantiateImpl() }
    )
        .fallbackToDestructiveMigration(true)
        .build()
}
