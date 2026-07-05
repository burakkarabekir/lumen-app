package com.bksd.profile.presentation

sealed interface ManagePremiumAction {
    data object OnUpgradeClick : ManagePremiumAction
    data object OnManageSubscriptionClick : ManagePremiumAction
    data object OnRestoreClick : ManagePremiumAction
}
