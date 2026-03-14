package com.bksd.paywall.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.paywall.presentation.components.FeatureRow
import com.bksd.paywall.presentation.components.HeroCard
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PaywallRoot(
    onDismiss: () -> Unit
) {
    val viewModel = koinViewModel<PaywallViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PaywallEvent.Dismiss -> onDismiss()
            PaywallEvent.SubscriptionSuccess -> onDismiss()
        }
    }

    PaywallScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun PaywallScreen(
    state: PaywallState,
    onAction: (PaywallAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(PaywallAction.OnCloseClick) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.content_desc_close),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = stringResource(Res.string.restore),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier
                    .clickable { onAction(PaywallAction.OnRestoreClick) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            HeroCard()

            Spacer(modifier = Modifier.height(32.dp))

            state.features.forEach { feature ->
                FeatureRow(feature = feature)
                Spacer(modifier = Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            state.tiers.forEach { tier ->
                PricingTierCard(
                    tier = tier,
                    isSelected = state.selectedTier?.id == tier.id,
                    onSelect = { onAction(PaywallAction.OnSelectTier(tier)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onAction(PaywallAction.OnSubscribeClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = !state.isProcessing
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    val ctaText = if (state.selectedTier?.hasFreeTrial == true) {
                        stringResource(Res.string.btn_start_trial)
                    } else {
                        stringResource(Res.string.btn_subscribe_now)
                    }
                    Text(
                        text = ctaText,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.legal_text),
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 0.5.sp,
                    lineHeight = 16.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
private fun PaywallScreenPreview() {
    val yearlyTier = BillingTierUi(
        id = "yearly",
        displayName = "Yearly Access",
        price = "$79.99",
        period = "/yr",
        subtitle = "7-DAY FREE TRIAL",
        monthlyBreakdown = "$6.66 per month",
        isPopularChoice = true,
        hasFreeTrial = true
    )

    PreviewAppTheme(darkTheme = true) {
        PaywallScreen(
            state = PaywallState(
                features = persistentListOf(
                    PaywallFeatureUi(
                        "Unlimited Multimedia",
                        "Rich entries with high-resolution photos and voice recordings."
                    ),
                    PaywallFeatureUi(
                        "AI Weekly Summaries",
                        "Personalized reflection insights delivered every Sunday morning."
                    ),
                    PaywallFeatureUi(
                        "Advanced Analytics",
                        "Visualize your emotional journey and mood trends over time."
                    )
                ),
                tiers = persistentListOf(
                    yearlyTier,
                    BillingTierUi(
                        id = "monthly",
                        displayName = "Monthly",
                        price = "$9.99",
                        period = "/mo",
                        subtitle = "Standard access"
                    )
                ),
                selectedTier = yearlyTier,
                isProcessing = false
            ),
            onAction = {}
        )
    }
}