package com.bksd.paywall.presentation

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PaywallState(
    val features: ImmutableList<PaywallFeatureUi> = persistentListOf(),
    val tiers: ImmutableList<BillingTierUi> = persistentListOf(),
    val selectedTier: BillingTierUi? = null,
    val isProcessing: Boolean = false,
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
)

data class PaywallFeatureUi(
    val title: String,
    val description: String,
    val icon: ImageVector
)

data class BillingTierUi(
    val id: String,
    val displayName: String,
    val price: String,
    val period: String,
    val subtitle: String,
    val monthlyBreakdown: String? = null,
    val badge: PaywallBadge? = null,
    val hasFreeTrial: Boolean = false
)

enum class PaywallBadge {
    POPULAR,
    BEST_VALUE
}
