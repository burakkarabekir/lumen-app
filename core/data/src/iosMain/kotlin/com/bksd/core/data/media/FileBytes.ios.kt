package com.bksd.core.data.media

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun readFileBytes(localPath: String): ByteArray {
    val data: NSData = NSData.dataWithContentsOfFile(localPath) ?: return ByteArray(0)
    val length = data.length.toInt()
    if (length == 0) return ByteArray(0)
    val bytes = ByteArray(length)
    bytes.usePinned { pinned ->
        memcpy(pinned.addressOf(0), data.bytes, length.toULong())
    }
    return bytes
}
