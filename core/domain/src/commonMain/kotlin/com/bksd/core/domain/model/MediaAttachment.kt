package com.bksd.core.domain.model

data class MediaAttachment(
    val id: String,
    val type: MediaType,
    val localPath: String? = null,
    val remoteUrl: String? = null,
    val durationMs: Long? = null,
    val sizeBytes: Long = 0
)

enum class MediaType {
    PHOTO, AUDIO, VIDEO
}
