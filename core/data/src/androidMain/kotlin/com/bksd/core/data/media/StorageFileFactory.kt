package com.bksd.core.data.media

import android.net.Uri
import dev.gitlive.firebase.storage.File

actual fun createStorageFile(localPath: String): File {
    return File(Uri.parse("file://$localPath"))
}
