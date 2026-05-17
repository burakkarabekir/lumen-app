package com.bksd.journal.presentation.detail

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

@Immutable
data class MomentDetailState(
    val isLoading: Boolean = false,
    val moment: Moment? = null,
    val error: UiText? = null,
    val isEditing: Boolean = false,
    val isBodyExpanded: Boolean = false,
    val editTitle: String = "",
    val editBody: String = "",
    val editMoods: ImmutableSet<Mood> = persistentSetOf(),
    val editTags: ImmutableList<String> = persistentListOf(),
    val isSaving: Boolean = false
)
