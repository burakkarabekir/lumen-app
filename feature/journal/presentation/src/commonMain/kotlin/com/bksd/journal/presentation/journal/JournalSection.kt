package com.bksd.journal.presentation.journal

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment
import kotlinx.collections.immutable.PersistentList

@Immutable
data class JournalSection(
    val id: String, // e.g. "2025-05-03"
    val dateHeader: UiText,
    val moments: PersistentList<Moment>
)
