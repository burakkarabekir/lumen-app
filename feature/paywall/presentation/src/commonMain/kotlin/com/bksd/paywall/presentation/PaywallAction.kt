package com.bksd.paywall.presentation

/**
 * User intents for the Paywall screen.
 */
sealed interface PaywallAction {
    data class OnSelectTier(val tier: BillingTier) : PaywallAction
    data object OnSubscribeClick : PaywallAction
    data object OnCloseClick : PaywallAction
    data object OnRestoreClick : PaywallAction
}
