package com.bksd.journal.presentation.model

import androidx.compose.runtime.Immutable
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.time.Instant

@Immutable
data class MomentUi(
    val id: String,
    val title: String,
    val body: String?,
    val createdAt: Instant,
    val moods: ImmutableList<Mood>,
    val tags: ImmutableList<String>,
    val attachments: ImmutableList<Attachment>,
    val location: LocationData?,
    val isFavorite: Boolean,
)

fun Moment.toMomentUi(): MomentUi = MomentUi(
    id = id,
    title = title,
    body = body,
    createdAt = createdAt,
    moods = moods.toImmutableList(),
    tags = tags.toImmutableList(),
    attachments = attachments.toImmutableList(),
    location = location,
    isFavorite = isFavorite,
)
