package com.bksd.paywall.presentation

sealed interface PaywallAction {
    data class OnSelectTier(val tier: BillingTierUi) : PaywallAction
    data object OnSubscribeClick : PaywallAction
    data object OnCloseClick : PaywallAction
    data object OnRestoreClick : PaywallAction
}
