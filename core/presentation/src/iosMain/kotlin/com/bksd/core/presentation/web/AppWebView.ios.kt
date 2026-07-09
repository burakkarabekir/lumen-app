package com.bksd.core.presentation.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun AppWebView(
    url: String,
    modifier: Modifier,
) {
    UIKitView(
        modifier = modifier,
        factory = {
            val webView = WKWebView(
                frame = CGRectZero.readValue(),
                configuration = WKWebViewConfiguration(),
            )
            NSURL.URLWithString(url)?.let { nsUrl ->
                webView.loadRequest(NSURLRequest(uRL = nsUrl))
            }
            webView
        },
    )
}
