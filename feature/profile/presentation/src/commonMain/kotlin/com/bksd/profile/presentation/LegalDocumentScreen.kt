package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.presentation.web.AppWebView
import org.jetbrains.compose.resources.stringResource

@Composable
fun LegalDocumentRoot(
    url: String,
    title: String,
    onBack: () -> Unit,
) {
    LegalDocumentScreen(
        url = url,
        title = title,
        onBack = onBack,
    )
}

@Composable
internal fun LegalDocumentScreen(
    url: String,
    title: String,
    onBack: () -> Unit,
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
            AppWebView(
                url = url,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
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
            onBack = {},
        )
    }
}
