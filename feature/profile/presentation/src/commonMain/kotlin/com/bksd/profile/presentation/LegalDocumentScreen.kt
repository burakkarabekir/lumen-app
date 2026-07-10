package com.bksd.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.presentation.web.AppWebView
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LegalDocumentRoot(
    url: String,
    title: String,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<LegalDocumentViewModel>()
    val state by viewModel.state.collectAsState()

    LegalDocumentScreen(
        url = url,
        title = title,
        state = state,
        onBack = onBack,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun LegalDocumentScreen(
    url: String,
    title: String,
    state: LegalDocumentState,
    onBack: () -> Unit,
    onAction: (LegalDocumentAction) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    AppScaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                title = title,
                style = AppBarStyle.Child,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.action_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { runCatching { uriHandler.openUri(url) } }) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = stringResource(Res.string.legal_open_external),
                        )
                    }
                },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                AppWebView(
                    url = url,
                    modifier = Modifier.fillMaxSize(),
                    reloadTrigger = state.reloadTrigger,
                    onLoadingChange = { loading ->
                        onAction(
                            if (loading) LegalDocumentAction.OnPageStarted
                            else LegalDocumentAction.OnPageFinished,
                        )
                    },
                    onError = { onAction(LegalDocumentAction.OnError) },
                )

                if (state.isError) {
                    Column(
                        modifier = Modifier
                            .matchParentSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(MaterialTheme.dimens.spacing.xl),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.legal_load_error),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
                        AppButton(
                            text = stringResource(Res.string.action_retry),
                            onClick = { onAction(LegalDocumentAction.OnRetry) },
                        )
                    }
                } else if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Preview
@Composable
private fun LegalDocumentScreenPreview() {
    PreviewAppTheme(darkTheme = true) {
        LegalDocumentScreen(
            url = "https://lumenjournalapp.com/terms",
            title = "Terms of Service",
            state = LegalDocumentState(isLoading = false),
            onBack = {},
            onAction = {},
        )
    }
}
