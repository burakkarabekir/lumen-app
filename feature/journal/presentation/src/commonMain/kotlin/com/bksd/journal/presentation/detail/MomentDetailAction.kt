package com.bksd.journal.presentation.detail

sealed interface MomentDetailAction {
    data class LoadMoment(val id: String) : MomentDetailAction
    data object OnNavigateBack : MomentDetailAction
    data object OnEditClick : MomentDetailAction
}
