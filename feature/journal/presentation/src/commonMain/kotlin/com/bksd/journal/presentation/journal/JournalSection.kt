package com.bksd.journal.presentation.journal

import androidx.compose.runtime.Immutable
import com.bksd.core.domain.model.Moment
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class JournalSection(
    val header: String,
    val items: ImmutableList<Moment>,
)
