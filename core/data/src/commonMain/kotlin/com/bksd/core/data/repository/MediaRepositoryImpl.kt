package com.bksd.core.data.repository

import com.bksd.core.data.remote.firebase.FirebaseStorageDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.MediaAttachment
import com.bksd.core.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val storageDataSource: FirebaseStorageDataSource
) : MediaRepository {

    override suspend fun uploadAttachment(
        attachment: MediaAttachment,
        userId: String,
        momentId: String
    ): Result<MediaAttachment, AppError> {
        val localPath = attachment.localPath ?: return Result.Success(attachment)

        // Remote path convention: users/{userId}/moments/{momentId}/{attachmentId}
        val extension = localPath.substringAfterLast('.', "")
        val remotePath = "users/$userId/moments/$momentId/${attachment.id}.$extension"

        return when (val result = storageDataSource.uploadFile(localPath, remotePath)) {
            is Result.Success -> {
                Result.Success(attachment.copy(remoteUrl = result.data))
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deleteAttachment(
        attachment: MediaAttachment
    ): Result<Unit, AppError> {
        val remoteUrl = attachment.remoteUrl ?: return Result.Success(Unit)

        // Extract the path from the URL or require the path explicitly
        // A safer way is to store the actual storage path on the attachment,
        // but for now we expect the data source to be able to delete by URL Ref (some SDKs support this)
        // Note: gitlive firebase storage might require the explicit path.
        // We will do a generic delete for now or assume remoteUrl contains enough info.
        return storageDataSource.deleteFile(remoteUrl)
    }
}
