package com.bksd.core.data.storage

import com.bksd.core.domain.storage.RecordingStorage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDomainMask

class InternalRecordingStorage : RecordingStorage {

    private val fileManager = NSFileManager.defaultManager

    @OptIn(ExperimentalForeignApi::class)
    private val recordingsDir: String
        get() {
            val documentsUrl = fileManager.URLsForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask
            ).firstOrNull() as? NSURL
            val recordingsPath = "${documentsUrl?.path}/recordings"
            fileManager.createDirectoryAtPath(recordingsPath, true, null, null)
            return recordingsPath
        }

    @OptIn(ExperimentalForeignApi::class)
    private val tempDir: String
        get() {
            val cachesUrl = fileManager.URLsForDirectory(
                NSCachesDirectory,
                NSUserDomainMask
            ).firstOrNull() as? NSURL
            val tempPath = "${cachesUrl?.path}/temp_recordings"
            fileManager.createDirectoryAtPath(tempPath, true, null, null)
            return tempPath
        }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun savePersistently(tempFilePath: String): String? =
        withContext(Dispatchers.IO) {
            try {
                if (!fileManager.fileExistsAtPath(tempFilePath)) return@withContext null

                val fileName =
                    "${RecordingStorage.PERSISTENT_FILE_PREFIX}_${NSUUID().UUIDString}.${RecordingStorage.RECORDING_FILE_EXTENSION}"
                val destinationPath = "$recordingsDir/$fileName"

                fileManager.copyItemAtPath(tempFilePath, destinationPath, null)
                fileManager.removeItemAtPath(tempFilePath, null)

                destinationPath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun cleanUpTemporaryFiles(): Unit = withContext(Dispatchers.IO) {
        val contents = fileManager.contentsOfDirectoryAtPath(tempDir, null) as? List<String>
        contents?.filter {
            it.startsWith(RecordingStorage.TEMP_FILE_PREFIX)
        }?.forEach {
            fileManager.removeItemAtPath("$tempDir/$it", null)
        }
    }
}
