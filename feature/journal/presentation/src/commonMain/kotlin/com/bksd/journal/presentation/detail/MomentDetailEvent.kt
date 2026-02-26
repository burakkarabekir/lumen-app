package com.bksd.journal.presentation.detail

import com.bksd.core.presentation.util.UiText

sealed interface MomentDetailEvent {
    data object NavigateBack : MomentDetailEvent
    data class NavigateToEdit(val momentId: String) : MomentDetailEvent
    data class ShowError(val error: UiText) : MomentDetailEvent
}
