package com.bksd.journal.presentation.journal

import androidx.compose.runtime.Immutable
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.JournalFilter
import com.bksd.journal.domain.model.Moment
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

@Immutable
data class JournalState(
    val moments: PersistentList<Moment> = persistentListOf(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val hasMorePages: Boolean = true,
    val isSyncing: Boolean = false,
    val error: UiText? = null,
    val selectedFilter: JournalFilter = JournalFilter.ALL,
    val searchQuery: String = "",
    val profilePhotoUrl: String? = null,
    val visibleDate: LocalDate,

    // Audio playback
    val playingAudioMomentId: String? = null,
    val audioPlaybackState: PlaybackState = PlaybackState.STOPPED,
    val audioCurrentPositionMs: Long = 0L
)
