package com.bksd.paywall.presentation.mapper

import com.bksd.paywall.domain.model.BillingPeriod
import com.bksd.paywall.domain.model.PaywallConfig
import com.bksd.paywall.domain.model.PremiumFeature
import com.bksd.paywall.domain.model.SubscriptionPlan
import com.bksd.paywall.presentation.BillingTierUi
import com.bksd.paywall.presentation.PaywallFeatureUi
import com.bksd.paywall.presentation.PaywallState
import kotlinx.collections.immutable.toImmutableList

fun PaywallConfig.toUiState(): PaywallState {
    val tiers = plans.map { it.toUi() }.toImmutableList()
    val defaultTier = tiers.firstOrNull { it.id == defaultPlanId } ?: tiers.first()

    return PaywallState(
        features = features.map { it.toUi() }.toImmutableList(),
        tiers = tiers,
        selectedTier = defaultTier
    )
}

fun PremiumFeature.toUi(): PaywallFeatureUi {
    return PaywallFeatureUi(
        title = title,
        description = description
    )
}

fun SubscriptionPlan.toUi(): BillingTierUi {
    val periodSuffix = when (period) {
        BillingPeriod.YEARLY -> "/yr"
        BillingPeriod.MONTHLY -> "/mo"
    }

    return BillingTierUi(
        id = id,
        displayName = name,
        price = price,
        period = periodSuffix,
        subtitle = subtitle,
        monthlyBreakdown = monthlyEquivalent,
        isPopularChoice = isPopularChoice,
        hasFreeTrial = hasFreeTrial
    )
}
