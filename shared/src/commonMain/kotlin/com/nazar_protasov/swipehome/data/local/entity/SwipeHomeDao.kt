package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SwipeHomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveListing(analytics: MyListingEntity)

    // Flow дозволяє UI автоматично оновлюватися при зміні даних у базі
    @Query("SELECT * FROM my_listings WHERE listingStatus = 'DRAFT' ORDER BY updateAt DESC")
    fun getAllDrafts(): Flow<List<MyListingEntity>>

    @Query("SELECT * FROM my_listings ORDER BY updateAt DESC")
    fun getAllMyCards(): Flow<List<MyListingEntity>>

    @Query("SELECT * FROM my_listings WHERE localId = :id")
    suspend fun getCardById(id: String): MyListingEntity?

    @Query("DELETE FROM my_listings WHERE localId = :id")
    suspend fun deleteCardById(id: String)

    // --- Робота зі свайпами --

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSwipe(swipe: SwipeEntity)

    // Отримуємо всі несинхронізовані свайпи, щоб відправити їх на бекенд
    @Query("SELECT * FROM swipes_history WHERE isSynced = 0")
    suspend fun getUnsyncedSwipe(): List<SwipeEntity>

    // Позначаємо свайп як відправлений та синхронізований з бекендом
    @Query("DELETE FROM swipes_history WHERE swipeId IN (:ids)")
    suspend fun markSwipeAsSynced(ids: List<String>)

    @Transaction
    @Query("SELECT * FROM my_listings ORDER BY updateAt DESC")
    fun getMyListing(): Flow<List<MyListingEntity>>

    @Transaction
    @Query("SELECT * FROM my_listings WHERE localId = :id")
    suspend fun getListWithAnalyticsById(id: String): MyListingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwipeHistory(items: List<HistoryPropertyEntity>)

    // Отримання списку за дією ("RIGHT" або "LEFT")
    @Query("SELECT * FROM swipe_history_properties p WHERE p.`action` = :action ORDER BY createsAt DESC")
    fun getSwipeHistoryByAction(action: String): Flow<List<HistoryPropertyEntity>>

    // Очищення або отримання всіх
    @Query("SELECT * FROM swipe_history_properties ORDER BY createsAt DESC")
    fun getAllSwipeHistory(): List<HistoryPropertyEntity>
}