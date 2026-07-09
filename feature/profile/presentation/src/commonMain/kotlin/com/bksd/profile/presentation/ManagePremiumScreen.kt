package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentAmber
import com.bksd.core.presentation.subscription.manageSubscriptionsUrl
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.profile.presentation.components.SettingsGroup
import com.bksd.profile.presentation.components.SettingsInfoRow
import com.bksd.profile.presentation.components.SettingsStatusHeader
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManagePremiumRoot(
    onBack: () -> Unit,
    onNavigateToPaywall: () -> Unit,
) {
    val viewModel = koinViewModel<ManagePremiumViewModel>()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ManagePremiumEvent.NavigateToPaywall -> onNavigateToPaywall()
            ManagePremiumEvent.OpenManageSubscriptions ->
                runCatching { uriHandler.openUri(manageSubscriptionsUrl()) }

            ManagePremiumEvent.RestoreSuccess -> scope.launch {
                snackbarHostState.showSnackbar(getString(Res.string.premium_restore_success))
            }

            ManagePremiumEvent.RestoreNone -> scope.launch {
                snackbarHostState.showSnackbar(getString(Res.string.premium_restore_none))
            }

            is ManagePremiumEvent.RestoreError -> scope.launch {
                snackbarHostState.showSnackbar(
                    getString(Res.string.premium_restore_error, event.message)
                )
            }
        }
    }

    ManagePremiumScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun ManagePremiumScreen(
    state: ManagePremiumState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onAction: (ManagePremiumAction) -> Unit,
) {
    AppScaffold(snackbarHostState = snackbarHostState) {
        AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.premium_title),
                    style = AppBarStyle.Child,
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.action_back),
                            )
                        }
                    },
                )
            },
        ) {
            SettingsStatusHeader(
                icon = Icons.Default.WorkspacePremium,
                title = stringResource(
                    if (state.isPlus) Res.string.premium_status_plus_title
                    else Res.string.premium_status_free_title
                ),
                subtitle = stringResource(
                    if (state.isPlus) Res.string.premium_status_plus_subtitle
                    else Res.string.premium_status_free_subtitle
                ),
                accent = MaterialTheme.colorScheme.extended.profileAccentAmber,
            )

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))

            SettingsGroup {
                SettingsInfoRow(
                    icon = Icons.Default.Star,
                    label = stringResource(Res.string.premium_plan),
                    value = stringResource(
                        if (state.isPlus) Res.string.plan_plus else Res.string.plan_free
                    ),
                    accent = MaterialTheme.colorScheme.extended.profileAccentAmber,
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            if (state.isPlus) {
                AppButton(
                    text = stringResource(Res.string.premium_manage_subscription),
                    onClick = { onAction(ManagePremiumAction.OnManageSubscriptionClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.spacing.lg)
                        .height(MaterialTheme.dimens.size.fab),
                    style = AppButtonStyle.PRIMARY,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.md),
                        )
                    },
                )
            } else {
                AppButton(
                    text = stringResource(Res.string.premium_upgrade),
                    onClick = { onAction(ManagePremiumAction.OnUpgradeClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.spacing.lg)
                        .height(MaterialTheme.dimens.size.fab),
                    style = AppButtonStyle.PRIMARY,
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))

            AppButton(
                text = stringResource(Res.string.premium_restore),
                onClick = { onAction(ManagePremiumAction.OnRestoreClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg)
                    .height(MaterialTheme.dimens.size.fab),
                isLoading = state.isRestoring,
                style = AppButtonStyle.SECONDARY,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.md),
                    )
                },
            )

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))

            Text(
                text = stringResource(Res.string.premium_manage_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.xl),
            )

            Spacer(Modifier.height(128.dp))
        }
    }
}

@Preview
@Composable
private fun ManagePremiumScreenFreePreview() {
    PreviewAppTheme(darkTheme = true) {
        ManagePremiumScreen(
            state = ManagePremiumState(isPlus = false),
            snackbarHostState = SnackbarHostState(),
            onBack = {},
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ManagePremiumScreenPlusPreview() {
    PreviewAppTheme(darkTheme = true) {
        ManagePremiumScreen(
            state = ManagePremiumState(isPlus = true),
            snackbarHostState = SnackbarHostState(),
            onBack = {},
            onAction = {},
        )
    }
}
