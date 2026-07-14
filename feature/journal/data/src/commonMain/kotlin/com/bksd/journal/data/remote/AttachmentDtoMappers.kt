package com.bksd.journal.data.remote

import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment

fun AttachmentDto.toAttachment(): Attachment {
    val attachmentId = AttachmentId(id)
    return when (this) {
        is AttachmentDto.Photo -> PhotoAttachment(
            id = attachmentId,
            remoteUrl = Url(remoteUrl),
            localUri = localUri,
            pendingUpload = pendingUpload
        )

        is AttachmentDto.Video -> VideoAttachment(
            id = attachmentId,
            remoteUrl = Url(remoteUrl),
            durationMs = durationMs,
            localUri = localUri,
            pendingUpload = pendingUpload
        )

        is AttachmentDto.Audio -> AudioAttachment(
            id = attachmentId,
            remoteUrl = Url(remoteUrl),
            durationMs = durationMs,
            localUri = localUri,
            pendingUpload = pendingUpload
        )

        is AttachmentDto.Link -> LinkAttachment(
            id = attachmentId,
            url = Url(url)
        )
    }
}

fun Attachment.toAttachmentDto(): AttachmentDto = when (this) {
    is PhotoAttachment -> AttachmentDto.Photo(
        id = id.value,
        remoteUrl = remoteUrl.value,
        localUri = localUri,
        pendingUpload = pendingUpload
    )

    is VideoAttachment -> AttachmentDto.Video(
        id = id.value,
        remoteUrl = remoteUrl.value,
        durationMs = durationMs,
        localUri = localUri,
        pendingUpload = pendingUpload
    )

    is AudioAttachment -> AttachmentDto.Audio(
        id = id.value,
        remoteUrl = remoteUrl.value,
        durationMs = durationMs,
        localUri = localUri,
        pendingUpload = pendingUpload
    )

    is LinkAttachment -> AttachmentDto.Link(
        id = id.value,
        url = url.value
    )
}
