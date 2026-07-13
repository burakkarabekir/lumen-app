package com.bksd.journal.data.local

import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.isPendingUpload
import com.bksd.journal.data.remote.toAttachmentDto
import kotlinx.serialization.json.Json

class DomainToEntityMapper(
    private val json: Json
) : Mapper<Moment, MomentEntity> {

    override fun map(input: Moment): MomentEntity = MomentEntity(
        id = input.id,
        title = input.title,
        body = input.body,
        createdAtMs = input.createdAt.toEpochMilliseconds(),
        moods = json.encodeToString(input.moods.map { it.name }),
        tags = json.encodeToString(input.tags),
        locationLatitude = input.location?.latitude,
        locationLongitude = input.location?.longitude,
        locationDisplayName = input.location?.displayName,
        attachments = json.encodeToString(input.attachments.map { it.toAttachmentDto() }),
        isFavorite = input.isFavorite,
        pendingSync = input.pendingSync,
        pendingUpload = input.attachments.any { it.isPendingUpload }
    )
}
