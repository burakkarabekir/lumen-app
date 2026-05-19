package com.bksd.journal.presentation.journal

import com.bksd.journal.domain.model.JournalFilter

sealed interface JournalAction {
    data class OnMomentClick(val id: String) : JournalAction
    data class OnFilterSelect(val filter: JournalFilter) : JournalAction
    data object OnCreateNewClick : JournalAction
    data class OnAudioPlayClick(val momentId: String, val audioUrl: String) : JournalAction
    data object OnAudioPauseClick : JournalAction
    data object OnLoadMore : JournalAction
    data class OnSearchQueryChange(val query: String) : JournalAction
    data object OnProfileClick : JournalAction
    data class OnDeleteMoment(val id: String) : JournalAction
    data class OnEditMoment(val id: String) : JournalAction
    data class OnFavoriteToggle(val id: String) : JournalAction
}
