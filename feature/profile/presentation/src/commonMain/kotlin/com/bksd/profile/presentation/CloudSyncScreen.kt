package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.bksd.core.design_system.theme.profileAccentGreen
import com.bksd.core.design_system.theme.profileAccentIndigo
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.profile.presentation.components.SettingsGroup
import com.bksd.profile.presentation.components.SettingsInfoRow
import com.bksd.profile.presentation.components.SettingsStatusHeader
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CloudSyncRoot(
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<CloudSyncViewModel>()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CloudSyncEvent.SyncSuccess -> scope.launch {
                snackbarHostState.showSnackbar(getString(Res.string.cloud_sync_success))
            }

            is CloudSyncEvent.SyncError -> scope.launch {
                snackbarHostState.showSnackbar(
                    getString(Res.string.cloud_sync_error, event.error.asStringAsync())
                )
            }
        }
    }

    CloudSyncScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun CloudSyncScreen(
    state: CloudSyncState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onAction: (CloudSyncAction) -> Unit,
) {
    AppScaffold(snackbarHostState = snackbarHostState) {
        AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.cloud_sync_title),
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
                icon = Icons.Default.CloudDone,
                title = stringResource(Res.string.cloud_sync_status_title),
                subtitle = stringResource(Res.string.cloud_sync_status_subtitle),
                accent = MaterialTheme.colorScheme.extended.profileAccentGreen,
            )

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))

            SettingsGroup {
                if (state.accountName.isNotBlank()) {
                    SettingsInfoRow(
                        icon = Icons.Default.Person,
                        label = stringResource(Res.string.cloud_sync_account),
                        value = state.accountName,
                        accent = MaterialTheme.colorScheme.extended.profileAccentIndigo,
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive),
                    )
                }
                SettingsInfoRow(
                    icon = Icons.Default.Description,
                    label = stringResource(Res.string.cloud_sync_entries),
                    value = state.entriesCount.toString(),
                    accent = MaterialTheme.colorScheme.extended.profileAccentIndigo,
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive),
                )
                SettingsInfoRow(
                    icon = Icons.Default.Shield,
                    label = stringResource(Res.string.cloud_sync_storage),
                    value = stringResource(Res.string.cloud_sync_storage_value),
                    accent = MaterialTheme.colorScheme.extended.profileAccentGreen,
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            AppButton(
                text = stringResource(Res.string.cloud_sync_now),
                onClick = { onAction(CloudSyncAction.OnSyncNowClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg)
                    .height(MaterialTheme.dimens.size.fab),
                isLoading = state.isSyncing,
                style = AppButtonStyle.PRIMARY,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.md),
                    )
                },
            )

            Spacer(Modifier.height(128.dp))
        }
    }
}

@Preview
@Composable
private fun CloudSyncScreenPreview() {
    PreviewAppTheme(darkTheme = true) {
        CloudSyncScreen(
            state = CloudSyncState(
                accountName = "Burak Yılmaz",
                entriesCount = 83,
            ),
            snackbarHostState = SnackbarHostState(),
            onBack = {},
            onAction = {},
        )
    }
}
