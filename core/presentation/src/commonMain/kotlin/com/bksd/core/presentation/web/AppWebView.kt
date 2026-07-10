package com.bksd.core.presentation.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun AppWebView(
    url: String,
    modifier: Modifier = Modifier,
    reloadTrigger: Int = 0,
    onLoadingChange: (Boolean) -> Unit = {},
    onError: () -> Unit = {},
)
