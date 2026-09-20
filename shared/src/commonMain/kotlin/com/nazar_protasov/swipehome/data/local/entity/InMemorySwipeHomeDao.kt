package com.nazar_protasov.swipehome.data.local.entity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemorySwipeHomeDao : SwipeHomeDao {
    private val _drafts = MutableStateFlow<List<DraftListingEntity>>(emptyList())
    private val _swipes = mutableListOf<SwipeEntity>()

    override suspend fun saveDraft(draft: DraftListingEntity) {
        val current = _drafts.value.toMutableList()
        current.removeAll { it.draftId == draft.draftId }
        current.add(0, draft)
        _drafts.value = current
    }

    override fun getAllDrafts(): Flow<List<DraftListingEntity>> = _drafts.asStateFlow()

    override fun getDraftById(id: String): DraftListingEntity? {
        return _drafts.value.find { it.draftId == id }
    }

    override suspend fun deleteDraftById(id: String) {
        val current = _drafts.value.toMutableList()
        current.removeAll { it.draftId == id }
        _drafts.value = current
    }

    override suspend fun saveSwipe(swipe: SwipeEntity) {
        _swipes.removeAll { it.swipeId == swipe.swipeId }
        _swipes.add(swipe)
    }

    override fun getUnsyncedSwipe(): List<SwipeEntity> {
        return _swipes.filter { !it.isSynced }
    }

    override suspend fun markSwipeAsSynced(ids: List<String>) {
        _swipes.removeAll { it.swipeId in ids }
    }
}
