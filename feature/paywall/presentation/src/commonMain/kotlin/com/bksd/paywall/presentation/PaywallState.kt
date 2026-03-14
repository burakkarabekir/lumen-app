package com.bksd.paywall.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PaywallState(
    val features: ImmutableList<PaywallFeatureUi> = persistentListOf(),
    val tiers: ImmutableList<BillingTierUi> = persistentListOf(),
    val selectedTier: BillingTierUi? = null,
    val isProcessing: Boolean = false
)

data class PaywallFeatureUi(
    val title: String,
    val description: String
)

data class BillingTierUi(
    val id: String,
    val displayName: String,
    val price: String,
    val period: String,
    val subtitle: String,
    val monthlyBreakdown: String? = null,
    val isPopularChoice: Boolean = false,
    val hasFreeTrial: Boolean = false
)
