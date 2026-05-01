package com.bksd.journal.data.local

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.data.remote.AttachmentDto
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import kotlinx.serialization.json.Json
import kotlin.time.Instant

class EntityToDomainMapper(
    private val json: Json
) : Mapper<MomentEntity, Moment> {

    override fun map(input: MomentEntity): Moment {
        val tagsList: List<String> = try {
            json.decodeFromString(input.tags)
        } catch (_: Exception) {
            emptyList()
        }

        val attachmentDtos: List<AttachmentDto> = try {
            json.decodeFromString(input.attachments)
        } catch (_: Exception) {
            emptyList()
        }

        return Moment(
            id = input.id,
            body = input.body,
            createdAt = Instant.fromEpochMilliseconds(input.createdAtMs),
            mood = Mood.entries.find { it.name == input.mood } ?: Mood.CALM,
            tags = tagsList,
            attachments = attachmentDtos.mapNotNull { it.toAttachment() },
            location = if (input.locationLatitude != null && input.locationLongitude != null) {
                LocationData(
                    latitude = input.locationLatitude,
                    longitude = input.locationLongitude,
                    displayName = input.locationDisplayName
                )
            } else null,
            pendingSync = input.pendingSync
        )
    }

    private fun AttachmentDto.toAttachment(): Attachment? {
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
}
