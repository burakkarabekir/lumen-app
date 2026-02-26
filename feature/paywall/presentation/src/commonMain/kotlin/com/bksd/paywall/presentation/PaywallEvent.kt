package com.bksd.paywall.presentation

/**
 * One-shot side-effects for the Paywall screen.
 */
sealed interface PaywallEvent {
    data object Dismiss : PaywallEvent
    data object SubscriptionSuccess : PaywallEvent
}
