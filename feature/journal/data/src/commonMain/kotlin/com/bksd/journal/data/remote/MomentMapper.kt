package com.bksd.journal.data.remote

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.Instant

fun MomentDto.toDomain(): Moment = Moment(
    id = id,
    body = body,
    createdAt = Instant.fromEpochMilliseconds(createdAtMs),
    mood = Mood.entries.find { it.name == mood } ?: Mood.CALM,
    tags = tags.toPersistentList(),
    attachments = attachments.mapNotNull { it.toDomain() }.toPersistentList(),
    location = location?.toDomain()
)

fun Moment.toDto(): MomentDto = MomentDto(
    id = id,
    body = body,
    createdAtMs = createdAt.toEpochMilliseconds(),
    mood = mood.name,
    tags = tags.toList(),
    location = location?.toDto(),
    attachments = attachments.map { it.toDto() }
)

private fun LocationDto.toDomain(): LocationData = LocationData(
    latitude = latitude,
    longitude = longitude,
    displayName = displayName
)

private fun LocationData.toDto(): LocationDto = LocationDto(
    latitude = latitude,
    longitude = longitude,
    displayName = displayName
)

private fun AttachmentDto.toDomain(): Attachment? {
    val attachmentId = AttachmentId(id)
    return when (type) {
        "PHOTO" -> PhotoAttachment(
            id = attachmentId,
            remoteUrl = Url(remoteUrl ?: "")
        )
        "VIDEO" -> VideoAttachment(
            id = attachmentId,
            remoteUrl = Url(remoteUrl ?: ""),
            durationMs = durationMs ?: 0L
        )
        "AUDIO" -> AudioAttachment(
            id = attachmentId,
            remoteUrl = Url(remoteUrl ?: ""),
            durationMs = durationMs ?: 0L
        )
        "LINK" -> LinkAttachment(
            id = attachmentId,
            url = Url(url ?: "")
        )
        else -> null
    }
}

private fun Attachment.toDto(): AttachmentDto = when (this) {
    is PhotoAttachment -> AttachmentDto(
        id = id.value,
        type = "PHOTO",
        remoteUrl = remoteUrl.value
    )
    is VideoAttachment -> AttachmentDto(
        id = id.value,
        type = "VIDEO",
        remoteUrl = remoteUrl.value,
        durationMs = durationMs
    )
    is AudioAttachment -> AttachmentDto(
        id = id.value,
        type = "AUDIO",
        remoteUrl = remoteUrl.value,
        durationMs = durationMs
    )
    is LinkAttachment -> AttachmentDto(
        id = id.value,
        type = "LINK",
        url = url.value
    )
}
