package com.bksd.core.data.remote.firebase

import com.bksd.core.data.media.createStorageData
import com.bksd.core.data.media.createStorageFile
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.MediaErrorType
import com.bksd.core.domain.error.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storageMetadata
import dev.gitlive.firebase.storage.storage

class FirebaseStorageDataSource {
    private val storage by lazy { Firebase.storage }

    suspend fun uploadBytes(
        bytes: ByteArray,
        remotePath: String,
        contentType: String = "image/jpeg"
    ): Result<String, AppError> {
        println("[FirebaseStorage] uploadBytes: remote=$remotePath, size=${bytes.size}, contentType=$contentType")
        return try {
            val storageRef = storage.reference.child(remotePath)
            val metadata = storageMetadata { this.contentType = contentType }
            storageRef.putData(createStorageData(bytes), metadata)
            val downloadUrl = storageRef.getDownloadUrl()
            println("[FirebaseStorage] uploadBytes: success, url=$downloadUrl")
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            println("[FirebaseStorage] uploadBytes: error=${e.message}")
            e.printStackTrace()
            Result.Error(mapStorageError(e))
        }
    }

    suspend fun uploadFile(
        localPath: String,
        remotePath: String
    ): Result<String, AppError> {
        println("[FirebaseStorage] uploadFile: local=$localPath, remote=$remotePath")
        return try {
            val storageRef = storage.reference.child(remotePath)
            val file = createStorageFile(localPath)
            storageRef.putFile(file)
            val downloadUrl = storageRef.getDownloadUrl()
            println("[FirebaseStorage] uploadFile: success, url=$downloadUrl")
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            println("[FirebaseStorage] uploadFile: error=${e.message}")
            e.printStackTrace()
            Result.Error(mapStorageError(e))
        }
    }

    suspend fun deleteFile(remotePath: String): Result<Unit, AppError> {
        println("[FirebaseStorage] deleteFile: $remotePath")
        return try {
            val storageRef = storage.reference.child(remotePath)
            storageRef.delete()
            println("[FirebaseStorage] deleteFile: success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[FirebaseStorage] deleteFile: error=${e.message}")
            e.printStackTrace()
            Result.Error(mapStorageError(e))
        }
    }

    private fun mapStorageError(e: Exception): AppError {
        val msg = e.message?.lowercase() ?: ""
        return when {
            msg.contains("permission") || msg.contains("authorized") ->
                AppError.Media(MediaErrorType.PERMISSION_DENIED)

            msg.contains("size") || msg.contains("large") ->
                AppError.Media(MediaErrorType.FILE_TOO_LARGE)

            else -> AppError.Media(MediaErrorType.UPLOAD_FAILED)
        }
    }
}
