package com.bksd.core.presentation.media

import com.bksd.core.domain.model.MediaType

sealed interface MediaPickResult {
    data class Success(val filePath: String, val type: MediaType, val sizeBytes: Long) :
        MediaPickResult

    data class Error(val message: String) : MediaPickResult
    data object Cancelled : MediaPickResult
}
