package com.bksd.journal.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MomentDto(
    val id: String,
    val title: String,
    val body: String? = null,
    @SerialName("created_at_ms") val createdAtMs: Long,
    val moods: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val location: LocationDto? = null,
    val attachments: List<AttachmentDto> = emptyList(),
    @SerialName("user_id") val userId: String? = null,
)

@Serializable
data class LocationDto(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val displayName: String? = null
)

@Serializable
sealed interface AttachmentDto {
    val id: String

    @Serializable
    @SerialName("PHOTO")
    data class Photo(
        override val id: String,
        val remoteUrl: String
    ) : AttachmentDto

    @Serializable
    @SerialName("VIDEO")
    data class Video(
        override val id: String,
        val remoteUrl: String,
        val durationMs: Long
    ) : AttachmentDto

    @Serializable
    @SerialName("AUDIO")
    data class Audio(
        override val id: String,
        val remoteUrl: String,
        val durationMs: Long
    ) : AttachmentDto

    @Serializable
    @SerialName("LINK")
    data class Link(
        override val id: String,
        val url: String
    ) : AttachmentDto
}
