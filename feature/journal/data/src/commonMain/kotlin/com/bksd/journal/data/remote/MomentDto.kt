package com.bksd.journal.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MomentDto(
    val id: String = "",
    val body: String? = null,
    val createdAtMs: Long = 0L,
    val mood: String = "",
    val tags: List<String> = emptyList(),
    val location: LocationDto? = null,
    val attachments: List<AttachmentDto> = emptyList()
)

@Serializable
data class LocationDto(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val displayName: String? = null
)

@Serializable
data class AttachmentDto(
    val id: String = "",
    val type: String = "",
    val remoteUrl: String? = null,
    val durationMs: Long? = null,
    val url: String? = null
)
