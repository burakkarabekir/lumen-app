package com.bksd.journal.presentation.journal

import com.bksd.core.presentation.util.UiText
import kotlinx.datetime.LocalDate

sealed interface JournalEvent {
    data class NavigateToDetail(val momentId: String) : JournalEvent
    data object NavigateToCreate : JournalEvent
    data class ShowError(val error: UiText) : JournalEvent
    data class ScrollToDate(val date: LocalDate) : JournalEvent
}
