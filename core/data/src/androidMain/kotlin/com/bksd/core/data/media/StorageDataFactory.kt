package com.bksd.core.data.media

import dev.gitlive.firebase.storage.Data

actual fun createStorageData(bytes: ByteArray): Data {
    return Data(bytes)
}
