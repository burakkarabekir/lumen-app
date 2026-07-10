package com.bksd.core.presentation.web

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun AppWebView(
    url: String,
    modifier: Modifier,
    reloadTrigger: Int,
    onLoadingChange: (Boolean) -> Unit,
    onError: () -> Unit,
) {
    val lastTrigger = remember { intArrayOf(reloadTrigger) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onLoadingChange(true)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onLoadingChange(false)
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?,
                    ) {
                        if (request?.isForMainFrame == true) {
                            onLoadingChange(false)
                            onError()
                        }
                    }
                }
                settings.javaScriptEnabled = false
                settings.domStorageEnabled = false
                loadUrl(url)
            }
        },
        update = { webView ->
            if (reloadTrigger != lastTrigger[0]) {
                lastTrigger[0] = reloadTrigger
                webView.reload()
            }
        },
        onRelease = { webView ->
            webView.stopLoading()
            webView.loadUrl("about:blank")
            (webView.parent as? ViewGroup)?.removeView(webView)
            webView.destroy()
        },
    )
}
