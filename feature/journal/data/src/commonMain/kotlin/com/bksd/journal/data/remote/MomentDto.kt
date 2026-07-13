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
    @SerialName("is_favorite") val isFavorite: Boolean = false,
    @SerialName("user_id") val userId: String? = null,
)

@Serializable
data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    val displayName: String? = null
)

@Serializable
sealed interface AttachmentDto {
    val id: String

    @Serializable
    @SerialName("PHOTO")
    data class Photo(
        override val id: String,
        val remoteUrl: String,
        val localUri: String? = null,
        val pendingUpload: Boolean = false
    ) : AttachmentDto

    @Serializable
    @SerialName("VIDEO")
    data class Video(
        override val id: String,
        val remoteUrl: String,
        val durationMs: Long,
        val localUri: String? = null,
        val pendingUpload: Boolean = false
    ) : AttachmentDto

    @Serializable
    @SerialName("AUDIO")
    data class Audio(
        override val id: String,
        val remoteUrl: String,
        val durationMs: Long,
        val localUri: String? = null,
        val pendingUpload: Boolean = false
    ) : AttachmentDto

    @Serializable
    @SerialName("LINK")
    data class Link(
        override val id: String,
        val url: String
    ) : AttachmentDto
}
