package com.bksd.paywall.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.graphics.vector.ImageVector
import com.bksd.paywall.domain.model.BillingPeriod
import com.bksd.paywall.domain.model.FeatureIcon
import com.bksd.paywall.domain.model.PaywallConfig
import com.bksd.paywall.domain.model.PlanBadge
import com.bksd.paywall.domain.model.PremiumFeature
import com.bksd.paywall.domain.model.SubscriptionPlan
import com.bksd.paywall.presentation.BillingTierUi
import com.bksd.paywall.presentation.PaywallBadge
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
        description = description,
        icon = icon.toImageVector()
    )
}

fun SubscriptionPlan.toUi(): BillingTierUi {
    val periodSuffix = when (period) {
        BillingPeriod.YEARLY -> "per year"
        BillingPeriod.MONTHLY -> "per month"
    }

    return BillingTierUi(
        id = id,
        displayName = name,
        price = price,
        period = periodSuffix,
        subtitle = subtitle,
        monthlyBreakdown = monthlyEquivalent,
        badge = badge?.toUi(),
        hasFreeTrial = hasFreeTrial
    )
}

private fun PlanBadge.toUi(): PaywallBadge = when (this) {
    PlanBadge.POPULAR -> PaywallBadge.POPULAR
    PlanBadge.BEST_VALUE -> PaywallBadge.BEST_VALUE
}

private fun FeatureIcon.toImageVector(): ImageVector = when (this) {
    FeatureIcon.MULTIMEDIA -> Icons.Default.Image
    FeatureIcon.AI_REFLECTION -> Icons.Default.AutoAwesome
    FeatureIcon.ANALYTICS -> Icons.AutoMirrored.Filled.ShowChart
}
