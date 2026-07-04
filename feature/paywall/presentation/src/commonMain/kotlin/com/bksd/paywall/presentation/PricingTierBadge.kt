package com.bksd.paywall.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.bestValueBadgeGradient
import com.bksd.core.design_system.theme.bestValueBadgeText
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import org.jetbrains.compose.resources.stringResource

@Composable
fun PricingTierBadge(
    badge: PaywallBadge,
    modifier: Modifier = Modifier
) {
    val brush = when (badge) {
        PaywallBadge.POPULAR -> SolidColor(MaterialTheme.colorScheme.primary)
        PaywallBadge.BEST_VALUE ->
            Brush.horizontalGradient(MaterialTheme.colorScheme.extended.bestValueBadgeGradient)
    }
    val textColor = when (badge) {
        PaywallBadge.POPULAR -> MaterialTheme.colorScheme.onPrimary
        PaywallBadge.BEST_VALUE -> MaterialTheme.colorScheme.extended.bestValueBadgeText
    }
    val label = when (badge) {
        PaywallBadge.POPULAR -> stringResource(Res.string.popular_choice)
        PaywallBadge.BEST_VALUE -> stringResource(Res.string.best_value)
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 8.sp,
            letterSpacing = 0.5.sp
        ),
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
            .background(brush)
            .padding(horizontal = MaterialTheme.dimens.spacing.sm, vertical = MaterialTheme.dimens.spacing.xxs)
    )
}

@Preview
@Composable
private fun PricingTierBadgePopularPreview() {
    PreviewAppTheme(darkTheme = true) {
        PricingTierBadge(badge = PaywallBadge.POPULAR)
    }
}

@Preview
@Composable
private fun PricingTierBadgeBestValuePreview() {
    PreviewAppTheme(darkTheme = true) {
        PricingTierBadge(badge = PaywallBadge.BEST_VALUE)
    }
}
