package com.bksd.journal.data.remote

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import kotlin.time.Instant

class MomentDtoMapper : Mapper<MomentDto, Moment> {

    override fun map(input: MomentDto): Moment = Moment(
        id = input.id,
        title = input.title,
        body = input.body,
        createdAt = Instant.fromEpochMilliseconds(input.createdAtMs),
        mood = Mood.entries.find { it.name == input.mood } ?: Mood.CALM,
        tags = input.tags,
        attachments = input.attachments.map { it.toAttachment() },
        location = input.location?.toLocationData()
    )

    private fun LocationDto.toLocationData(): LocationData = LocationData(
        latitude = latitude,
        longitude = longitude,
        displayName = displayName
    )

    private fun AttachmentDto.toAttachment(): Attachment {
        val attachmentId = AttachmentId(id)
        return when (this) {
            is AttachmentDto.Photo -> PhotoAttachment(
                id = attachmentId,
                remoteUrl = Url(remoteUrl)
            )

            is AttachmentDto.Video -> VideoAttachment(
                id = attachmentId,
                remoteUrl = Url(remoteUrl),
                durationMs = durationMs
            )

            is AttachmentDto.Audio -> AudioAttachment(
                id = attachmentId,
                remoteUrl = Url(remoteUrl),
                durationMs = durationMs
            )

            is AttachmentDto.Link -> LinkAttachment(
                id = attachmentId,
                url = Url(url)
            )
        }
    }
}
