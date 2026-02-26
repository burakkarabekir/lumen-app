package com.bksd.core.data.storage

import android.content.Context
import com.bksd.core.domain.storage.RecordingStorage
import com.bksd.core.domain.storage.RecordingStorage.Companion.PERSISTENT_DIRECTORY_PREFIX
import com.bksd.core.domain.storage.RecordingStorage.Companion.TEMP_FILE_PREFIX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class InternalRecordingStorage(
    private val context: Context
) : RecordingStorage {

    private val recordingsDir: File
        get() = File(context.filesDir, PERSISTENT_DIRECTORY_PREFIX).apply { mkdirs() }

    private val tempDir: File
        get() = File(context.cacheDir, TEMP_FILE_PREFIX).apply { mkdirs() }

    override suspend fun savePersistently(tempFilePath: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val tempFile = File(tempFilePath)
                if (!tempFile.exists()) return@withContext null

                val fileName =
                    "${RecordingStorage.PERSISTENT_FILE_PREFIX}_${UUID.randomUUID()}.${RecordingStorage.RECORDING_FILE_EXTENSION}"
                val destinationFile = File(recordingsDir, fileName)

                tempFile.copyTo(destinationFile, overwrite = true)
                tempFile.delete()

                destinationFile.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun cleanUpTemporaryFiles(): Unit = withContext(Dispatchers.IO) {
        tempDir.listFiles()?.filter {
            it.name.startsWith(TEMP_FILE_PREFIX)
        }?.forEach { it.delete() }
    }
}
