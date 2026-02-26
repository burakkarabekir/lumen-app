package com.bksd.journal.presentation.journal

sealed interface JournalAction {
    data object LoadMoments : JournalAction
    data class OnMomentClick(val id: String) : JournalAction
    data class OnFilterSelect(val filter: String) : JournalAction
    data object OnCreateNewClick : JournalAction
}
