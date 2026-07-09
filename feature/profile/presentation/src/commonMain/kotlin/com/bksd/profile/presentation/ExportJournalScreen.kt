package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
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
import com.bksd.core.design_system.theme.profileAccentGreen
import com.bksd.core.presentation.share.rememberPdfShareAction
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.profile.presentation.components.SettingsStatusHeader
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExportJournalRoot(
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<ExportJournalViewModel>()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val pdfShare = rememberPdfShareAction()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ExportJournalEvent.SharePdf -> pdfShare.share(event.bytes, event.fileName)

            ExportJournalEvent.Empty -> scope.launch {
                snackbarHostState.showSnackbar(getString(Res.string.export_empty))
            }

            is ExportJournalEvent.Error -> scope.launch {
                snackbarHostState.showSnackbar(
                    getString(Res.string.export_error, event.message)
                )
            }
        }
    }

    ExportJournalScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun ExportJournalScreen(
    state: ExportJournalState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onAction: (ExportJournalAction) -> Unit,
) {
    AppScaffold(snackbarHostState = snackbarHostState) {
        AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.export_title),
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
                icon = Icons.Default.PictureAsPdf,
                title = stringResource(Res.string.export_intro_title),
                subtitle = stringResource(Res.string.export_intro_subtitle),
                accent = MaterialTheme.colorScheme.extended.profileAccentGreen,
            )

            Text(
                text = if (state.entriesCount > 0) {
                    stringResource(Res.string.export_entries_count, state.entriesCount)
                } else {
                    stringResource(Res.string.export_empty)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.xl),
            )

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            AppButton(
                text = stringResource(Res.string.export_button),
                onClick = { onAction(ExportJournalAction.OnExportClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg)
                    .height(MaterialTheme.dimens.size.fab),
                enabled = state.entriesCount > 0,
                isLoading = state.isExporting,
                style = AppButtonStyle.PRIMARY,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Download,
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
private fun ExportJournalScreenPreview() {
    PreviewAppTheme(darkTheme = true) {
        ExportJournalScreen(
            state = ExportJournalState(entriesCount = 83),
            snackbarHostState = SnackbarHostState(),
            onBack = {},
            onAction = {},
        )
    }
}
