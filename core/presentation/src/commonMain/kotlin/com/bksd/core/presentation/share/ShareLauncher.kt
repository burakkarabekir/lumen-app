package com.bksd.core.presentation.share

class ShareLauncher(
    private val onShare: (String) -> Unit
) {
    fun share(text: String) = onShare(text)
}
