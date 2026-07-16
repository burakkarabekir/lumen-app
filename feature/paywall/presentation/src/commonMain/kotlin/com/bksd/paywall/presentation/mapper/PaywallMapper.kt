package com.bksd.paywall.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Videocam
import com.bksd.core.presentation.util.UiText
import com.bksd.paywall.domain.model.PaywallConfig
import com.bksd.paywall.domain.model.PremiumFeature
import com.bksd.paywall.presentation.PaywallFeatureUi
import com.bksd.paywall.presentation.PaywallState
import com.bksd.paywall.presentation.Res
import com.bksd.paywall.presentation.paywall_feature_media_desc
import com.bksd.paywall.presentation.paywall_feature_media_title
import com.bksd.paywall.presentation.paywall_feature_reflection_desc
import com.bksd.paywall.presentation.paywall_feature_reflection_title
import kotlinx.collections.immutable.toImmutableList

fun PaywallConfig.toUiState(): PaywallState = PaywallState(
    features = features.map { it.toUi() }.toImmutableList(),
    isLoading = true
)

fun PremiumFeature.toUi(): PaywallFeatureUi = when (this) {
    PremiumFeature.MULTIMEDIA -> PaywallFeatureUi(
        title = UiText.Resource(Res.string.paywall_feature_media_title),
        description = UiText.Resource(Res.string.paywall_feature_media_desc),
        icon = Icons.Default.Videocam
    )

    PremiumFeature.AI_REFLECTION -> PaywallFeatureUi(
        title = UiText.Resource(Res.string.paywall_feature_reflection_title),
        description = UiText.Resource(Res.string.paywall_feature_reflection_desc),
        icon = Icons.Default.AutoAwesome
    )
}
