package com.bksd.journal.presentation.journal

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment

@Immutable
data class JournalState(
    val moments: List<Moment> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val selectedFilter: String = "All Entries"
)
