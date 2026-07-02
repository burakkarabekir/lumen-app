package com.bksd.journal.data.remote

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.mapper.Mapper
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import kotlin.time.Instant

class MomentDtoMapper : Mapper<MomentDto, Moment> {

    override fun map(input: MomentDto): Moment = Moment(
        id = input.id,
        title = input.title,
        body = input.body,
        createdAt = Instant.fromEpochMilliseconds(input.createdAtMs),
        moods = input.moods.mapNotNull { name -> Mood.entries.find { it.name == name } },
        tags = input.tags,
        attachments = input.attachments.map { it.toAttachment() },
        location = input.location?.toLocationData(),
        isFavorite = input.isFavorite
    )

    private fun LocationDto.toLocationData(): LocationData = LocationData(
        latitude = latitude,
        longitude = longitude,
        displayName = displayName
    )
}
