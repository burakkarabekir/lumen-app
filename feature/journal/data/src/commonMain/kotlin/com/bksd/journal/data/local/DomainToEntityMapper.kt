package com.bksd.journal.data.local

import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.data.remote.AttachmentDto
import com.bksd.journal.domain.model.Moment
import kotlinx.serialization.json.Json

class DomainToEntityMapper(
    private val json: Json
) : Mapper<Moment, MomentEntity> {

    override fun map(input: Moment): MomentEntity = MomentEntity(
        id = input.id,
        body = input.body,
        createdAtMs = input.createdAt.toEpochMilliseconds(),
        mood = input.mood.name,
        tags = json.encodeToString(input.tags),
        locationLatitude = input.location?.latitude,
        locationLongitude = input.location?.longitude,
        locationDisplayName = input.location?.displayName,
        attachments = json.encodeToString(input.attachments.map { it.toAttachmentDto() }),
        pendingSync = input.pendingSync
    )

    private fun Attachment.toAttachmentDto(): AttachmentDto = when (this) {
        is PhotoAttachment -> AttachmentDto(
            id = id.value, type = "PHOTO", remoteUrl = remoteUrl.value
        )

        is VideoAttachment -> AttachmentDto(
            id = id.value, type = "VIDEO", remoteUrl = remoteUrl.value, durationMs = durationMs
        )

        is AudioAttachment -> AttachmentDto(
            id = id.value, type = "AUDIO", remoteUrl = remoteUrl.value, durationMs = durationMs
        )

        is LinkAttachment -> AttachmentDto(
            id = id.value, type = "LINK", url = url.value
        )
    }
}
