package com.bksd.core.data.storage

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class PlatformFileStorage(
    private val context: Context
) {
    actual suspend fun saveImage(bytes: ByteArray, fileName: String): String {
        return withContext(Dispatchers.IO) {
            val dir = File(context.filesDir, "profile")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, fileName)
            file.writeBytes(bytes)
            file.absolutePath
        }
    }
}
