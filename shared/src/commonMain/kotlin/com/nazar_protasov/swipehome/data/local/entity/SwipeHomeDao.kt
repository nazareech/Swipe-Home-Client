package com.nazar_protasov.swipehome.data.local.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SwipeHomeDao {
    // --- Робота над чернетками ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: DraftListingEntity)

    // Flow дозволяє UI автоматично оновлюватися при зміні даних у базі
    @Query("SELECT * FROM draft_listings ORDER BY updateAt DESC")
    fun getAllDrafts(): Flow<List<DraftListingEntity>>

    @Query("SELECT * FROM draft_listings WHERE draftId = :id")
    suspend fun getDraftById(id: String): DraftListingEntity?

    @Query("SELECT * FROM analytics_listing WHERE analyticId = :id")
    suspend fun getCardById(id: String): AnalyticsListingEntity?

    @Query("DELETE FROM draft_listings WHERE draftId = :id")
    suspend fun deleteDraftById(id: String)

    // --- Робота зі свайпами --

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSwipe(swipe: SwipeEntity)

    // Отримуємо всі несинхронізовані свайпи, щоб відправити їх на бекенд
    @Query("SELECT * FROM swipes_history WHERE isSynced = 0")
    suspend fun getUnsyncedSwipe(): List<SwipeEntity>

    // Позначаємо свайп як відправлений та синхронізований з бекендом
    @Query("DELETE FROM swipes_history WHERE swipeId IN (:ids)")
    suspend fun markSwipeAsSynced(ids: List<String>)
}