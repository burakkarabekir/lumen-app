package com.bksd.paywall.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme

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
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelect() },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor
            ),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tier.displayName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tier.subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (tier.hasFreeTrial) FontWeight.SemiBold else FontWeight.Normal,
                            letterSpacing = if (tier.hasFreeTrial) 0.5.sp else 0.sp
                        ),
                        color = if (tier.hasFreeTrial && isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        }
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = tier.price,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = tier.period,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(bottom = 3.dp)
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

        if (tier.isPopularChoice) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "POPULAR CHOICE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
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
                displayName = "Yearly Access",
                price = "$79.99",
                period = "/yr",
                subtitle = "7-DAY FREE TRIAL",
                monthlyBreakdown = "$6.66 per month",
                isPopularChoice = true,
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
                period = "/mo",
                subtitle = "Standard access"
            ),
            isSelected = false,
            onSelect = {}
        )
    }
}
