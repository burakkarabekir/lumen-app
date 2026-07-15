package com.bksd.paywall.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens

@Composable
fun PricingTierCard(
    tier: BillingTierUi,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            onClick = onSelect,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(MaterialTheme.dimens.radius.lg),
            border = BorderStroke(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor
            ),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.spacing.lg,
                    vertical = MaterialTheme.dimens.spacing.lg
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.icon.lg)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                        .border(
                            width = 1.5.dp,
                            color = if (isSelected) {
                                Color.Transparent
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tier.displayName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxs))
                    Text(
                        text = tier.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (tier.hasFreeTrial && isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        }
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.md))

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = tier.price,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.xxs))
                        Text(
                            text = tier.period,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(bottom = MaterialTheme.dimens.spacing.xs)
                        )
                    }
                    tier.monthlyBreakdown?.let { breakdown ->
                        Text(
                            text = breakdown,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }

        tier.badge?.let { badge ->
            PricingTierBadge(
                badge = badge,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(
                        x = -MaterialTheme.dimens.spacing.lg,
                        y = -MaterialTheme.dimens.spacing.sm
                    )
            )
        }
    }
}

@Preview
@Composable
private fun PricingTierCardYearlyPreview() {
    PreviewAppTheme(darkTheme = true) {
        PricingTierCard(
            tier = BillingTierUi(
                id = "yearly",
                displayName = "Yearly",
                price = "$79.99",
                period = "per year",
                subtitle = "5-day free trial · $6.66/mo",
                badge = PaywallBadge.BEST_VALUE,
                hasFreeTrial = true
            ),
            isSelected = true,
            onSelect = {}
        )
    }
}

@Preview
@Composable
private fun PricingTierCardMonthlyPreview() {
    PreviewAppTheme(darkTheme = true) {
        PricingTierCard(
            tier = BillingTierUi(
                id = "monthly",
                displayName = "Monthly",
                price = "$9.99",
                period = "per month",
                subtitle = "Billed monthly",
                badge = PaywallBadge.POPULAR
            ),
            isSelected = false,
            onSelect = {}
        )
    }
}
