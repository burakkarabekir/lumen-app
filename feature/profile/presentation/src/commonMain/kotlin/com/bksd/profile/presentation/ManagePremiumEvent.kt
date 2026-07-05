package com.bksd.profile.presentation

sealed interface ManagePremiumEvent {
    data object NavigateToPaywall : ManagePremiumEvent
    data object OpenManageSubscriptions : ManagePremiumEvent
    data object RestoreSuccess : ManagePremiumEvent
    data object RestoreNone : ManagePremiumEvent
    data class RestoreError(val message: String) : ManagePremiumEvent
}
