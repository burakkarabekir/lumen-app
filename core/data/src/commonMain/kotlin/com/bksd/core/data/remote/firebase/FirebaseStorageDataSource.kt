package com.bksd.core.data.remote.firebase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.MediaErrorType
import com.bksd.core.domain.error.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage

class FirebaseStorageDataSource {
    private val storage by lazy { Firebase.storage }

    suspend fun uploadFile(
        localPath: String,
        remotePath: String
    ): Result<String, AppError> {
        return try {
            val storageRef = storage.reference.child(remotePath)
            // KMP Firebase Storage expects dev.gitlive.firebase.storage.File
            // We use the expect/actual bridge to create the platform-specific File
            val file = com.bksd.core.data.media.createStorageFile(localPath)
            storageRef.putFile(file)
            val downloadUrl = storageRef.getDownloadUrl()
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(mapStorageError(e))
        }
    }

    suspend fun deleteFile(remotePath: String): Result<Unit, AppError> {
        return try {
            val storageRef = storage.reference.child(remotePath)
            storageRef.delete()
            Result.Success(Unit)
        } catch (e: Exception) {
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
