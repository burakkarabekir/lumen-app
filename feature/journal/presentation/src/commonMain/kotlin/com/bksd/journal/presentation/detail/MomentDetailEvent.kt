package com.bksd.journal.presentation.detail

import com.bksd.core.presentation.util.UiText

sealed interface MomentDetailEvent {
    data object NavigateBack : MomentDetailEvent
    data class ShowError(val error: UiText) : MomentDetailEvent
    data class ShowSuccess(val message: UiText) : MomentDetailEvent
}
