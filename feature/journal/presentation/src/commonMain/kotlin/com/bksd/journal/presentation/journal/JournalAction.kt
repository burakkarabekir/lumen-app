package com.bksd.journal.presentation.journal

import kotlinx.datetime.LocalDate

sealed interface JournalAction {
    data class OnMomentClick(val id: String) : JournalAction
    data class OnFilterSelect(val filter: String) : JournalAction
    data class OnDateSelect(val date: LocalDate) : JournalAction
    data object OnCreateNewClick : JournalAction
}
