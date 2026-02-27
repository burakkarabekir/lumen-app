package com.bksd.journal.presentation.journal

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

@Immutable
data class JournalState(
    val moments: PersistentList<Moment> = persistentListOf(),
    val filteredMoments: PersistentList<Moment> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val selectedFilter: String = "All Entries",
    val selectedDate: LocalDate? = null
)
