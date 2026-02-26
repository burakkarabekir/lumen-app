package com.bksd.core.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class RecordingDetails(
    val duration: Duration = Duration.ZERO,
    val amplitudes: ImmutableList<Float> = persistentListOf(),
    val filePath: String? = null
)