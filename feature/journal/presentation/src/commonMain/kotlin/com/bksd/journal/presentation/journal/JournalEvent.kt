package com.bksd.journal.presentation.journal

import com.bksd.core.presentation.util.UiText

sealed interface JournalEvent {
    data class NavigateToDetail(val momentId: String, val isEditing: Boolean = false) : JournalEvent
    data object NavigateToProfile : JournalEvent
    data class ShowError(val error: UiText) : JournalEvent
}
