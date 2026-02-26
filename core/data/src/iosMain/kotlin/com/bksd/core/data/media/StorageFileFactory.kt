package com.bksd.core.data.media

import dev.gitlive.firebase.storage.File
import platform.Foundation.NSURL

actual fun createStorageFile(localPath: String): File {
    return File(NSURL.fileURLWithPath(localPath))
}
