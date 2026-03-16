package com.bksd.core.data.media

import dev.gitlive.firebase.storage.Data
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.create

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun createStorageData(bytes: ByteArray): Data {
    val nsData = memScoped {
        NSData.create(
            bytes = allocArrayOf(bytes),
            length = bytes.size.toULong()
        )
    }
    return Data(nsData)
}
