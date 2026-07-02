package com.bksd.journal.data.remote

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Moment

class MomentToDtoMapper : Mapper<Moment, MomentDto> {

    override fun map(input: Moment): MomentDto = MomentDto(
        id = input.id,
        title = input.title,
        body = input.body,
        createdAtMs = input.createdAt.toEpochMilliseconds(),
        moods = input.moods.map { it.name },
        tags = input.tags,
        location = input.location?.toDto(),
        attachments = input.attachments.map { it.toAttachmentDto() },
        isFavorite = input.isFavorite
    )

    private fun LocationData.toDto(): LocationDto = LocationDto(
        latitude = latitude,
        longitude = longitude,
        displayName = displayName
    )
}
