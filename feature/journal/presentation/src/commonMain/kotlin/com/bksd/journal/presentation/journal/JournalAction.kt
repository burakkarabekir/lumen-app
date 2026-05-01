package com.bksd.journal.presentation.journal

import com.bksd.journal.domain.model.JournalFilter
import kotlinx.datetime.LocalDate

sealed interface JournalAction {
    data class OnMomentClick(val id: String) : JournalAction
    data class OnFilterSelect(val filter: JournalFilter) : JournalAction
    data class OnDateSelect(val date: LocalDate) : JournalAction
    data object OnCreateNewClick : JournalAction
}
