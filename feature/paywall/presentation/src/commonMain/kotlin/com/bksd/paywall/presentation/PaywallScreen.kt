package com.bksd.paywall.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.paywall.presentation.components.FeatureRow
import com.bksd.paywall.presentation.components.HeroCard
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val TERMS_URL = "https://lumen.app/terms"
private const val PRIVACY_URL = "https://lumen.app/privacy"

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
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.spacing.sm, vertical = MaterialTheme.dimens.spacing.sm),
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
                    .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.sm)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MaterialTheme.dimens.spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.sm))

            HeroCard()

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxxl))

            state.features.forEach { feature ->
                FeatureRow(feature = feature)
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xl))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.sm))

            state.tiers.forEach { tier ->
                PricingTierCard(
                    tier = tier,
                    isSelected = state.selectedTier?.id == tier.id,
                    onSelect = { onAction(PaywallAction.OnSelectTier(tier)) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

            AppButton(
                text = if (state.selectedTier?.hasFreeTrial == true) {
                    stringResource(Res.string.btn_start_trial)
                } else {
                    stringResource(Res.string.btn_subscribe_now)
                },
                onClick = { onAction(PaywallAction.OnSubscribeClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.size.fab),
                enabled = !state.isProcessing,
                isLoading = state.isProcessing,
                style = AppButtonStyle.PRIMARY
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

            Text(
                text = stringResource(Res.string.legal_text),
                style = MaterialTheme.typography.labelSmall.copy(
                    lineHeight = 16.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.terms_of_use),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { runCatching { uriHandler.openUri(TERMS_URL) } }
                )
                Text(
                    text = "   ·   ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
                Text(
                    text = stringResource(Res.string.privacy_policy),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { runCatching { uriHandler.openUri(PRIVACY_URL) } }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxxl))
        }
    }
}

@Preview
@Composable
private fun PaywallScreenPreview() {
    val yearlyTier = BillingTierUi(
        id = "yearly",
        displayName = "Yearly",
        price = "$79.99",
        period = "per year",
        subtitle = "5-day free trial · $6.66/mo",
        badge = PaywallBadge.BEST_VALUE,
        hasFreeTrial = true
    )

    PreviewAppTheme(darkTheme = true) {
        PaywallScreen(
            state = PaywallState(
                features = persistentListOf(
                    PaywallFeatureUi(
                        title = "Unlimited Multimedia",
                        description = "Enrich entries with high-resolution photos, video, and voice recordings.",
                        icon = Icons.Default.Image
                    ),
                    PaywallFeatureUi(
                        title = "AI Weekly Reflections",
                        description = "Personal, written insights on your moods and themes every Sunday.",
                        icon = Icons.Default.AutoAwesome
                    ),
                    PaywallFeatureUi(
                        title = "Advanced Analytics",
                        description = "Visualize your emotional journey and mood trends over time.",
                        icon = Icons.AutoMirrored.Filled.ShowChart
                    )
                ),
                tiers = persistentListOf(
                    yearlyTier,
                    BillingTierUi(
                        id = "monthly",
                        displayName = "Monthly",
                        price = "$9.99",
                        period = "per month",
                        subtitle = "Billed monthly",
                        badge = PaywallBadge.POPULAR
                    )
                ),
                selectedTier = yearlyTier,
                isProcessing = false
            ),
            onAction = {}
        )
    }
}