package com.bksd.core.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class Url(val value: String)

data class PhotoAttachment(
    override val id: AttachmentId,
    val remoteUrl: Url,
    val localUri: String? = null,
    val pendingUpload: Boolean = false
) : Attachment

data class VideoAttachment(
    override val id: AttachmentId,
    val remoteUrl: Url,
    val durationMs: Long,
    val localUri: String? = null,
    val pendingUpload: Boolean = false
) : Attachment

data class AudioAttachment(
    override val id: AttachmentId,
    val remoteUrl: Url,
    val durationMs: Long,
    val localUri: String? = null,
    val pendingUpload: Boolean = false
) : Attachment

data class LinkAttachment(
    override val id: AttachmentId,
    val url: Url
) : Attachment

val Attachment.isPendingUpload: Boolean
    get() = when (this) {
        is PhotoAttachment -> pendingUpload
        is VideoAttachment -> pendingUpload
        is AudioAttachment -> pendingUpload
        is LinkAttachment -> false
    }

enum class MediaType {
    PHOTO, AUDIO, VIDEO, LINK
}
