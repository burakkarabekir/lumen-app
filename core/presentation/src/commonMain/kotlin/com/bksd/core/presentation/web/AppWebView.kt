package com.bksd.core.presentation.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Renders a web page at [url] inside the app.
 * Android uses [android.webkit.WebView]; iOS uses WKWebView.
 */
@Composable
expect fun AppWebView(
    url: String,
    modifier: Modifier = Modifier,
)
