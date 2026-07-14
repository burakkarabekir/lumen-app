@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.bksd.core.data.storage

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile
import kotlin.coroutines.cancellation.CancellationException

actual class PlatformFileStorage {
    actual suspend fun saveImage(bytes: ByteArray, fileName: String): String {
        return withContext(Dispatchers.IO) {
            val paths = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            )
            val documentsDir = paths.first() as String
            val profileDir = "$documentsDir/profile"

            val fileManager = NSFileManager.defaultManager
            if (!fileManager.fileExistsAtPath(profileDir)) {
                fileManager.createDirectoryAtPath(
                    profileDir,
                    withIntermediateDirectories = true,
                    attributes = null,
                    error = null
                )
            }

            val filePath = "$profileDir/$fileName"
            val nsData = bytes.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
            }
            nsData.writeToFile(filePath, atomically = true)
            filePath
        }
    }

    actual suspend fun persistToFiles(localPath: String): String = withContext(Dispatchers.IO) {
        try {
            val fileManager = NSFileManager.defaultManager
            if (!fileManager.fileExistsAtPath(localPath)) return@withContext localPath
            val documentsDir = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).first() as String
            val dir = "$documentsDir/pending_media"
            if (!fileManager.fileExistsAtPath(dir)) {
                fileManager.createDirectoryAtPath(dir, true, null, null)
            }
            val extension = localPath.substringAfterLast('.', "")
            val name = NSUUID().UUIDString + if (extension.isNotEmpty()) ".$extension" else ""
            val destination = "$dir/$name"
            fileManager.copyItemAtPath(localPath, destination, null)
            destination
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            localPath
        }
    }
}
