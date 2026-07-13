package com.bksd.core.data.storage

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

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

    actual suspend fun persistToFiles(localPath: String): String = withContext(Dispatchers.IO) {
        try {
            val source = File(localPath)
            if (!source.exists()) return@withContext localPath
            val dir = File(context.filesDir, "pending_media").apply { mkdirs() }
            if (source.parentFile == dir) return@withContext localPath
            val extension = localPath.substringAfterLast('.', "")
            val name = UUID.randomUUID().toString() + if (extension.isNotEmpty()) ".$extension" else ""
            val destination = File(dir, name)
            source.copyTo(destination, overwrite = true)
            destination.absolutePath
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            localPath
        }
    }
}
