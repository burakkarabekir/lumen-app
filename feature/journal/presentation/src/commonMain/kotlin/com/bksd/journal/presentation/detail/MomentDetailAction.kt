package com.bksd.journal.presentation.detail

import com.bksd.core.domain.model.Mood

sealed interface MomentDetailAction {
    data object OnNavigateBack : MomentDetailAction
    data object OnMenuClick : MomentDetailAction
    data object OnEditClick : MomentDetailAction
    data object OnSaveChanges : MomentDetailAction
    data object OnCancelEdit : MomentDetailAction
    data object OnDeleteClick : MomentDetailAction
    data object OnShareClick : MomentDetailAction
    data object OnFavoriteToggle : MomentDetailAction
    data object OnToggleBodyExpand : MomentDetailAction
    data class OnTitleChange(val title: String) : MomentDetailAction
    data class OnBodyChange(val body: String) : MomentDetailAction
    data class OnMoodToggle(val mood: Mood) : MomentDetailAction
    data class OnTagRemove(val tag: String) : MomentDetailAction
}
