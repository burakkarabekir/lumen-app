package com.bksd.journal.data.local

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import com.bksd.journal.data.remote.AttachmentDto
import com.bksd.journal.data.remote.toAttachment
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

        val moodsList: List<Mood> = try {
            val names: List<String> = json.decodeFromString(input.moods)
            names.mapNotNull { name -> Mood.entries.find { it.name == name } }
        } catch (_: Exception) {
            // Backward compat: try parsing as a single mood string
            val singleMood = Mood.entries.find { it.name == input.moods }
            if (singleMood != null) listOf(singleMood) else emptyList()
        }

        return Moment(
            id = input.id,
            title = input.title,
            body = input.body,
            createdAt = Instant.fromEpochMilliseconds(input.createdAtMs),
            moods = moodsList,
            tags = tagsList,
            attachments = attachmentDtos.map { it.toAttachment() },
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
}
