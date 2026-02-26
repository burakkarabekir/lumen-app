package com.bksd.core.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.MediaAttachment

interface MediaRepository {
    suspend fun uploadAttachment(
        attachment: MediaAttachment,
        userId: String,
        momentId: String
    ): Result<MediaAttachment, AppError>

    suspend fun deleteAttachment(
        attachment: MediaAttachment
    ): Result<Unit, AppError>
}
