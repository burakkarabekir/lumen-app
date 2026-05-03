package com.bksd.journal.data.remote

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.domain.model.Moment

class MomentToDtoMapper : Mapper<Moment, MomentDto> {

    override fun map(input: Moment): MomentDto = MomentDto(
        id = input.id,
        body = input.body,
        createdAtMs = input.createdAt.toEpochMilliseconds(),
        mood = input.mood.name,
        tags = input.tags,
        location = input.location?.toDto(),
        attachments = input.attachments.map { it.toDto() }
    )

    private fun LocationData.toDto(): LocationDto = LocationDto(
        latitude = latitude,
        longitude = longitude,
        displayName = displayName
    )

    private fun Attachment.toDto(): AttachmentDto = when (this) {
        is PhotoAttachment -> AttachmentDto.Photo(
            id = id.value,
            remoteUrl = remoteUrl.value
        )

        is VideoAttachment -> AttachmentDto.Video(
            id = id.value,
            remoteUrl = remoteUrl.value,
            durationMs = durationMs
        )

        is AudioAttachment -> AttachmentDto.Audio(
            id = id.value,
            remoteUrl = remoteUrl.value,
            durationMs = durationMs
        )

        is LinkAttachment -> AttachmentDto.Link(
            id = id.value,
            url = url.value
        )
    }
}
