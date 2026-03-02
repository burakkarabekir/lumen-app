package com.bksd.core.data.repository

import com.bksd.core.data.remote.firebase.FirebaseStorageDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.DraftAttachment
import com.bksd.core.domain.model.DraftAudio
import com.bksd.core.domain.model.DraftLink
import com.bksd.core.domain.model.DraftPhoto
import com.bksd.core.domain.model.DraftVideo
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val storageDataSource: FirebaseStorageDataSource
) : MediaRepository {

    override suspend fun uploadAttachment(
        draft: DraftAttachment,
        userId: String,
        momentId: String
    ): Result<Attachment, AppError> {
        return when (draft) {
            is DraftLink -> Result.Success(
                LinkAttachment(
                    id = draft.id,
                    url = draft.url
                )
            )

            is DraftPhoto -> {
                uploadAndMap(draft.id, draft.localUri, userId, momentId) { remoteUrl ->
                    PhotoAttachment(id = draft.id, remoteUrl = Url(remoteUrl))
                }
            }

            is DraftVideo -> {
                uploadAndMap(draft.id, draft.localUri, userId, momentId) { remoteUrl ->
                    VideoAttachment(id = draft.id, remoteUrl = Url(remoteUrl), durationMs = draft.durationMs)
                }
            }

            is DraftAudio -> {
                uploadAndMap(draft.id, draft.localUri, userId, momentId) { remoteUrl ->
                    AudioAttachment(id = draft.id, remoteUrl = Url(remoteUrl), durationMs = draft.durationMs)
                }
            }
        }
    }

    private suspend inline fun uploadAndMap(
        attachmentId: AttachmentId,
        localPath: String,
        userId: String,
        momentId: String,
        mapper: (String) -> Attachment
    ): Result<Attachment, AppError> {
        // Remote path convention: users/{userId}/moments/{momentId}/{attachmentId}
        val extension = localPath.substringAfterLast('.', "")
        val remotePath = "users/$userId/moments/$momentId/${attachmentId.value}.$extension"

        return when (val result = storageDataSource.uploadFile(localPath, remotePath)) {
            is Result.Success -> Result.Success(mapper(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deleteAttachment(
        attachment: Attachment
    ): Result<Unit, AppError> {
        val remoteUrl = when (attachment) {
            is PhotoAttachment -> attachment.remoteUrl.value
            is VideoAttachment -> attachment.remoteUrl.value
            is AudioAttachment -> attachment.remoteUrl.value
            is LinkAttachment -> return Result.Success(Unit) 
        }

        return storageDataSource.deleteFile(remoteUrl)
    }
}
