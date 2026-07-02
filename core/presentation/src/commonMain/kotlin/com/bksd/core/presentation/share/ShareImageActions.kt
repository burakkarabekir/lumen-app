package com.bksd.core.presentation.share

class ShareImageActions(
    private val onShare: (ByteArray) -> Unit,
    private val onSave: (ByteArray) -> Boolean,
    private val onCopy: (ByteArray) -> Boolean,
) {
    fun share(png: ByteArray) = onShare(png)
    fun save(png: ByteArray): Boolean = onSave(png)
    fun copy(png: ByteArray): Boolean = onCopy(png)
}
