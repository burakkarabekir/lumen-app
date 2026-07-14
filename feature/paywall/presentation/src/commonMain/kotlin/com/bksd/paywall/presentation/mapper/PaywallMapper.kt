package com.bksd.paywall.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.graphics.vector.ImageVector
import com.bksd.paywall.domain.model.FeatureIcon
import com.bksd.paywall.domain.model.PaywallConfig
import com.bksd.paywall.domain.model.PremiumFeature
import com.bksd.paywall.presentation.PaywallFeatureUi
import com.bksd.paywall.presentation.PaywallState
import kotlinx.collections.immutable.toImmutableList

fun PaywallConfig.toUiState(): PaywallState = PaywallState(
    features = features.map { it.toUi() }.toImmutableList(),
    isLoading = true
)

fun PremiumFeature.toUi(): PaywallFeatureUi = PaywallFeatureUi(
    title = title,
    description = description,
    icon = icon.toImageVector()
)

private fun FeatureIcon.toImageVector(): ImageVector = when (this) {
    FeatureIcon.MULTIMEDIA -> Icons.Default.Image
    FeatureIcon.AI_REFLECTION -> Icons.Default.AutoAwesome
    FeatureIcon.ANALYTICS -> Icons.AutoMirrored.Filled.ShowChart
}
