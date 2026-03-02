package com.bksd.core.domain.model

data class DraftPhoto(
    override val id: AttachmentId,
    val localUri: String,
    val sizeBytes: Long
) : DraftAttachment

data class DraftVideo(
    override val id: AttachmentId,
    val localUri: String,
    val durationMs: Long,
    val sizeBytes: Long
) : DraftAttachment

data class DraftAudio(
    override val id: AttachmentId,
    val localUri: String,
    val durationMs: Long,
    val sizeBytes: Long
) : DraftAttachment

data class DraftLink(
    override val id: AttachmentId,
    val url: Url
) : DraftAttachment