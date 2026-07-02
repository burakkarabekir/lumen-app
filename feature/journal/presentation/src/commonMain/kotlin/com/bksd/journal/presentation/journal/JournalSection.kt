package com.bksd.journal.presentation.journal

import androidx.compose.runtime.Immutable
import com.bksd.journal.presentation.model.MomentUi
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class JournalSection(
    val header: String,
    val items: ImmutableList<MomentUi>,
)
