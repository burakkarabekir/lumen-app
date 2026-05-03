@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bksd.journal.presentation.journal

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.JournalFilter
import com.bksd.journal.domain.model.applyFilter
import com.bksd.journal.domain.usecase.GetMomentsByRangeUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.section_today
import com.bksd.journal.presentation.section_yesterday
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class JournalViewModel(
    private val getMomentsByRangeUseCase: GetMomentsByRangeUseCase,
    private val syncMomentsUseCase: SyncMomentsUseCase,
    private val clock: Clock,
    private val timeZone: TimeZone,
    private val audioPlayer: AudioPlayer
) : BaseViewModel<JournalAction, JournalEvent>() {

    companion object {
        private const val INITIAL_DAYS_TO_LOAD = 14
        private const val LOAD_MORE_DAYS = 7
    }

    private val today get() = clock.todayIn(timeZone)

    private val oldestLoadedDate = MutableStateFlow(
        today.minus(INITIAL_DAYS_TO_LOAD - 1, DateTimeUnit.DAY)
    )
    private val visibleDate = MutableStateFlow(today)
    private val selectedDate = MutableStateFlow(today)
    private val selectedFilter = MutableStateFlow(JournalFilter.ALL)
    private val isSyncing = MutableStateFlow(false)
    private val isLoadingMore = MutableStateFlow(false)
    private val error = MutableStateFlow<UiText?>(null)
    private val playingAudioMomentId = MutableStateFlow<String?>(null)

    private var hasLoadedInitialData = false

    val state: StateFlow<JournalState> = combine(
        combine(
            oldestLoadedDate.flatMapLatest { oldest ->
                getMomentsByRangeUseCase(oldest, today)
            },
            selectedFilter,
            visibleDate,
            selectedDate
        ) { moments, filter, visible, selected ->
            val filtered = moments.applyFilter(filter)
            val currentToday = today

            val sections = filtered
                .groupBy { moment ->
                    moment.createdAt.toLocalDateTime(timeZone).date
                }
                .entries
                .sortedByDescending { it.key }
                .map { (date, dayMoments) ->
                    JournalSection(
                        id = date.toString(),
                        dateHeader = formatSectionHeader(date, currentToday),
                        moments = dayMoments
                            .sortedByDescending { it.createdAt }
                            .toPersistentList()
                    )
                }
                .toPersistentList()

            JournalState(
                sections = sections,
                isLoading = false,
                visibleDate = visible,
                selectedDate = selected,
                selectedFilter = filter
            )
        },
        combine(
            isSyncing,
            isLoadingMore,
            error
        ) { syncing, loadingMore, err ->
            Triple(syncing, loadingMore, err)
        },
        playingAudioMomentId,
        audioPlayer.playbackState,
        audioPlayer.currentPositionMs
    ) { baseState, (syncing, loadingMore, err), playingId, playbackState, positionMs ->
        baseState.copy(
            isSyncing = syncing,
            isLoadingMore = loadingMore,
            error = err,
            playingAudioMomentId = playingId,
            audioPlaybackState = playbackState,
            audioCurrentPositionMs = positionMs
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                syncInitialDays()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = JournalState(
                isLoading = true,
                visibleDate = today,
                selectedDate = today
            )
        )

    override fun onAction(action: JournalAction) {
        when (action) {
            JournalAction.OnCreateNewClick -> sendEvent(JournalEvent.NavigateToCreate)

            is JournalAction.OnFilterSelect -> selectedFilter.update { action.filter }

            is JournalAction.OnMomentClick -> sendEvent(JournalEvent.NavigateToDetail(action.id))

            is JournalAction.OnDateSelect -> {
                selectedDate.update { action.date }
                visibleDate.update { action.date }
                // Expand loaded range if needed
                if (action.date < oldestLoadedDate.value) {
                    oldestLoadedDate.update { action.date }
                    syncFromRemote(action.date)
                }
                sendEvent(JournalEvent.ScrollToDate(action.date))
            }

            is JournalAction.OnAudioPlayClick -> {
                playingAudioMomentId.update { action.momentId }
                launch { audioPlayer.play(action.audioUrl) }
            }

            JournalAction.OnAudioPauseClick -> {
                launch { audioPlayer.pause() }
            }

            JournalAction.OnLoadMoreDays -> {
                if (isLoadingMore.value) return
                launch {
                    isLoadingMore.update { true }
                    val currentOldest = oldestLoadedDate.value
                    val newOldest = currentOldest.minus(LOAD_MORE_DAYS, DateTimeUnit.DAY)

                    // Sync each new day from remote
                    var day = newOldest
                    while (day < currentOldest) {
                        syncFromRemote(day)
                        day = day.plus(1, DateTimeUnit.DAY)
                    }

                    oldestLoadedDate.update { newOldest }
                    isLoadingMore.update { false }
                }
            }

            is JournalAction.OnVisibleDateChanged -> {
                visibleDate.update { action.date }
            }
        }
    }

    private fun formatSectionHeader(date: LocalDate, today: LocalDate): UiText {
        val daysAgo = today.toEpochDays() - date.toEpochDays()
        return when (daysAgo) {
            0L -> UiText.Resource(Res.string.section_today)
            1L -> UiText.Resource(Res.string.section_yesterday)
            else -> {
                val monthName = date.month.name.lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                if (date.year == today.year) {
                    UiText.Dynamic(monthName)
                } else {
                    UiText.Dynamic("$monthName ${date.year}")
                }
            }
        }
    }

    private fun syncInitialDays() {
        launch {
            isSyncing.update { true }
            error.update { null }

            val currentToday = today
            var day = oldestLoadedDate.value
            while (day <= currentToday) {
                syncFromRemote(day)
                day = day.plus(1, DateTimeUnit.DAY)
            }

            isSyncing.update { false }
        }
    }

    private fun syncFromRemote(date: LocalDate) {
        launch {
            when (val result = syncMomentsUseCase(date)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    error.update { errorText }
                    sendEvent(JournalEvent.ShowError(errorText))
                }

                is Result.Success -> { /* Room Flow auto-updates the UI */
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
