package com.bksd.core.presentation.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun AppWebView(
    url: String,
    modifier: Modifier,
    reloadTrigger: Int,
    onLoadingChange: (Boolean) -> Unit,
    onError: () -> Unit,
) {
    val delegate = remember { AppWebViewNavigationDelegate() }
    delegate.onLoadingChange = onLoadingChange
    delegate.onError = onError

    val lastTrigger = remember { intArrayOf(reloadTrigger) }

    UIKitView(
        modifier = modifier,
        factory = {
            val webView = WKWebView(
                frame = CGRectZero.readValue(),
                configuration = WKWebViewConfiguration(),
            )
            webView.navigationDelegate = delegate
            NSURL.URLWithString(url)?.let { nsUrl ->
                webView.loadRequest(NSURLRequest(uRL = nsUrl))
            }
            webView
        },
        update = { webView ->
            if (reloadTrigger != lastTrigger[0]) {
                lastTrigger[0] = reloadTrigger
                webView.reload()
            }
        },
    )
}

@OptIn(ExperimentalForeignApi::class)
private class AppWebViewNavigationDelegate :
    NSObject(),
    WKNavigationDelegateProtocol {

    var onLoadingChange: (Boolean) -> Unit = {}
    var onError: () -> Unit = {}

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        onLoadingChange(true)
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        onLoadingChange(false)
    }

    @ObjCSignatureOverride
    override fun webView(
        webView: WKWebView,
        didFailNavigation: WKNavigation?,
        withError: NSError,
    ) {
        onLoadingChange(false)
        onError()
    }

    @ObjCSignatureOverride
    override fun webView(
        webView: WKWebView,
        didFailProvisionalNavigation: WKNavigation?,
        withError: NSError,
    ) {
        onLoadingChange(false)
        onError()
    }
}
