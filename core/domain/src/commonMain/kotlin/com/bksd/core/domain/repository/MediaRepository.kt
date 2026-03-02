package com.bksd.core.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.DraftAttachment

interface MediaRepository {
    suspend fun uploadAttachment(
        draft: DraftAttachment,
        userId: String,
        momentId: String
    ): Result<Attachment, AppError>

    suspend fun deleteAttachment(
        attachment: Attachment
    ): Result<Unit, AppError>
}
