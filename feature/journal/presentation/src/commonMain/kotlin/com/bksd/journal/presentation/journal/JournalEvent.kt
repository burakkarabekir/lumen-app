package com.bksd.journal.presentation.journal

import com.bksd.core.presentation.util.UiText

sealed interface JournalEvent {
    data class NavigateToDetail(val momentId: String) : JournalEvent
    data object NavigateToCreate : JournalEvent
    data class ShowError(val error: UiText) : JournalEvent
}
