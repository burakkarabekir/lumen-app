package com.bksd.core.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class Url(val value: String)

data class PhotoAttachment(
    override val id: AttachmentId,
    val remoteUrl: Url
) : Attachment

data class VideoAttachment(
    override val id: AttachmentId,
    val remoteUrl: Url,
    val durationMs: Long
) : Attachment

data class AudioAttachment(
    override val id: AttachmentId,
    val remoteUrl: Url,
    val durationMs: Long
) : Attachment

data class LinkAttachment(
    override val id: AttachmentId,
    val url: Url
) : Attachment

enum class MediaType {
    PHOTO, AUDIO, VIDEO, LINK
}
