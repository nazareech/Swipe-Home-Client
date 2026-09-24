package com.nazar_protasov.swipehome.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.nazar_protasov.swipehome.data.local.entity.SwipeHomeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

expect fun getRoomDatabaseBuilder() : RoomDatabase.Builder<SwipeHomeDatabase>

fun getRoomDatabase() : SwipeHomeDatabase {
    return getRoomDatabaseBuilder()
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver()) // Використовуємо нативний драйвер
        .setQueryCoroutineContext(Dispatchers.IO) // Усі запити йтимуть у фоновому потоці
        .build()
}