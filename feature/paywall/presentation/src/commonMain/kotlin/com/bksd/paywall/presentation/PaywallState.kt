package com.bksd.paywall.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Immutable UI state for the Momentum Premium Paywall screen.
 */
data class PaywallState(
    val features: ImmutableList<PaywallFeature> = persistentListOf(),
    val selectedTier: BillingTier = BillingTier.YEARLY,
    val isProcessing: Boolean = false
)

data class PaywallFeature(
    val title: String,
    val description: String
)

enum class BillingTier(
    val displayName: String,
    val price: String,
    val period: String,
    val subtitle: String,
    val monthlyBreakdown: String? = null,
    val isBestValue: Boolean = false,
    val hasFreeTrial: Boolean = false
) {
    YEARLY(
        displayName = "Yearly Access",
        price = "$79.99",
        period = "/yr",
        subtitle = "7-day free trial included",
        monthlyBreakdown = "$6.66 / month",
        isBestValue = true,
        hasFreeTrial = true
    ),
    MONTHLY(
        displayName = "Monthly Access",
        price = "$9.99",
        period = "/mo",
        subtitle = "Pay as you go"
    )
}
