package com.bksd.journal.presentation.detail

sealed interface MomentDetailAction {
    data object OnNavigateBack : MomentDetailAction
    data object OnEditClick : MomentDetailAction
}
