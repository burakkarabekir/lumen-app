package com.bksd.journal.presentation.journal

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.applyFilter
import com.bksd.journal.domain.usecase.DeleteMomentUseCase
import com.bksd.journal.domain.usecase.GetPagedMomentsUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import com.bksd.journal.presentation.journal.JournalViewModel.Companion.LOAD_MORE_SIZE
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class JournalViewModel(
    private val getPagedMomentsUseCase: GetPagedMomentsUseCase,
    private val syncMomentsUseCase: SyncMomentsUseCase,
    private val deleteMomentUseCase: DeleteMomentUseCase,
    private val sessionStorage: SessionStorage,
    private val clock: Clock,
    private val timeZone: TimeZone,
    private val audioPlayer: AudioPlayer
) : BaseViewModel<JournalAction, JournalEvent>() {

    companion object {
        private const val INITIAL_PAGE_SIZE = 20
        private const val LOAD_MORE_SIZE = 10
    }

    private val today get() = clock.todayIn(timeZone)

    /** Single mutable state — all reducer mutations go through _state.update {} */
    private val _state = MutableStateFlow(
        JournalState(
            isLoading = true,
            visibleDate = today,
            profilePhotoUrl = sessionStorage.getProfilePhotoUrl()
        )
    )

    private var currentPageSize = INITIAL_PAGE_SIZE
    private var observeJob: Job? = null

    init {
        syncFromRemote(limit = INITIAL_PAGE_SIZE, offset = 0)
        observeMoments()
    }

    /**
     * Exposed state combines the reducer state with audio player reactive streams,
     * keeping the audio concern reactive without polluting the single _state flow.
     */
    val state: StateFlow<JournalState> = combine(
        _state,
        audioPlayer.playbackState,
        audioPlayer.currentPositionMs
    ) { currentState, playbackState, positionMs ->
        currentState.copy(
            audioPlaybackState = playbackState,
            audioCurrentPositionMs = positionMs
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = _state.value
    )

    override fun onAction(action: JournalAction) {
        when (action) {
            JournalAction.OnCreateNewClick -> sendEvent(JournalEvent.NavigateToCreate)

            is JournalAction.OnMomentClick -> sendEvent(JournalEvent.NavigateToDetail(action.id))

            is JournalAction.OnFilterSelect -> {
                _state.update { it.copy(selectedFilter = action.filter) }
                reloadMoments()
            }

            is JournalAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
                reloadMoments()
            }

            JournalAction.OnLoadMore -> loadMore()

            is JournalAction.OnAudioPlayClick -> {
                _state.update { it.copy(playingAudioMomentId = action.momentId) }
                launch { audioPlayer.play(action.audioUrl) }
            }

            JournalAction.OnAudioPauseClick -> {
                launch { audioPlayer.pause() }
            }

            JournalAction.OnProfileClick -> {
                // Handled by the screen via event if needed
            }

            is JournalAction.OnDeleteMoment -> {
                launch {
                    val result = deleteMomentUseCase(action.id)
                    if (result is Result.Error) {
                        sendEvent(JournalEvent.ShowError(UiText.Dynamic(result.error.toString())))
                    }
                }
            }
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Data loading
    // ──────────────────────────────────────────────────────────────

    /**
     * Starts (or restarts) a coroutine that observes Room's paged query.
     * The query is LIMIT-based: we always query [currentPageSize] items.
     * When the user loads more, we bump the page size and restart observation.
     */
    private fun observeMoments() {
        observeJob?.cancel()
        observeJob = launch {
            getPagedMomentsUseCase(limit = currentPageSize, offset = 0)
                .collect { moments ->
                    _state.update { current ->
                        val filtered = moments.applyFilter(current.selectedFilter)
                        val searched = applySearch(filtered, current.searchQuery)

                        current.copy(
                            moments = searched.toPersistentList(),
                            isLoading = false,
                            isLoadingMore = false,
                            hasMorePages = moments.size >= currentPageSize,
                        )
                    }
                }
        }
    }

    /**
     * Bumps the page size by [LOAD_MORE_SIZE], syncs the next page from
     * Firestore, and restarts Room observation to include the new data.
     */
    private fun loadMore() {
        val current = _state.value
        if (current.isLoadingMore || !current.hasMorePages) return

        _state.update { it.copy(isLoadingMore = true) }

        val previousSize = currentPageSize
        currentPageSize += LOAD_MORE_SIZE

        // Sync the next chunk from remote, then restart local observation
        syncFromRemote(limit = LOAD_MORE_SIZE, offset = previousSize)
        observeMoments()
    }

    /**
     * Re-applies filter/search on the current page size without changing it.
     */
    private fun reloadMoments() {
        observeMoments()
    }

    /**
     * Fetches [limit] moments from Firestore starting at [offset]
     * and upserts them into Room.
     */
    private fun syncFromRemote(limit: Int, offset: Int) {
        launch {
            _state.update { it.copy(isSyncing = true, error = null) }
            when (val result = syncMomentsUseCase(limit = limit, offset = offset)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    _state.update { it.copy(isSyncing = false, error = errorText) }
                    sendEvent(JournalEvent.ShowError(errorText))
                }

                is Result.Success -> {
                    _state.update { it.copy(isSyncing = false) }
                }
            }
        }
    }

    private fun applySearch(moments: List<Moment>, query: String): List<Moment> {
        if (query.isBlank()) return moments
        return moments.filter { moment ->
            moment.title.contains(query, ignoreCase = true) ||
                    (moment.body?.contains(query, ignoreCase = true) == true) ||
                    moment.tags.any { it.contains(query, ignoreCase = true) } ||
                    moment.moods.any { it.label.contains(query, ignoreCase = true) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
