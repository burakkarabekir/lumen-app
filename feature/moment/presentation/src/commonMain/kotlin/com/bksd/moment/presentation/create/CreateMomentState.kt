package com.bksd.moment.presentation.create

import androidx.compose.runtime.Stable
import com.bksd.core.domain.model.MediaType
import com.bksd.core.domain.model.PlaybackState
import com.bksd.journal.domain.model.Mood
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate

data class CreateMomentState(
    val body: String = "",
    val selectedMood: Mood? = null,
    val tags: PersistentList<String> = persistentListOf(),
    val location: LocationInfoUiModel? = null,
    val isFetchingLocation: Boolean = false,
    val isSaving: Boolean = false,
    val selectedDate: LocalDate? = null,

    // Media capabilities
    val isAttachmentsExpanded: Boolean = true,
    
    // Explicit slots to enforce 1 per type
    val photoAttachment: AttachmentUiModel.Photo? = null,
    val videoAttachment: AttachmentUiModel.Video? = null,
    val audioAttachment: AttachmentUiModel.Audio? = null,
    val linkAttachment: AttachmentUiModel.Link? = null,
    
    val recordingState: RecordingUiState = RecordingUiState.Idle,
    val isRecordingSheetVisible: Boolean = false,
    val timestampFormatted: String = "Today",
    val linkInput: String = "",
    val isLinkSheetVisible: Boolean = false,

    // Audio playback
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val playbackAmplitudes: ImmutableList<Float> = persistentListOf(),
    val playbackPositionFormatted: String = "0:00",
    val playbackDurationFormatted: String = "0:00"
) {
    val allAttachments: PersistentList<AttachmentUiModel>
        get() = listOfNotNull(
            photoAttachment, videoAttachment, audioAttachment, linkAttachment
        ).toPersistentList()
}

@Stable
sealed interface AttachmentUiModel {
    val id: String
    val type: MediaType
    val displayName: String
    val isUploading: Boolean

    data class Photo(
        override val id: String,
        val localPath: String? = null,
        val remoteUrl: String? = null,
        val sizeBytes: Long = 0L,
        override val displayName: String = "Photo",
        override val isUploading: Boolean = false
    ) : AttachmentUiModel {
        override val type = MediaType.PHOTO
    }

    data class Video(
        override val id: String,
        val localPath: String? = null,
        val remoteUrl: String? = null,
        val durationMs: Long = 0L,
        val sizeBytes: Long = 0L,
        override val displayName: String = "Video",
        override val isUploading: Boolean = false
    ) : AttachmentUiModel {
        override val type = MediaType.VIDEO
    }

    data class Audio(
        override val id: String,
        val localPath: String? = null,
        val remoteUrl: String? = null,
        val durationMs: Long = 0L,
        override val displayName: String = "Voice Note",
        override val isUploading: Boolean = false
    ) : AttachmentUiModel {
        override val type = MediaType.AUDIO
    }

    data class Link(
        override val id: String,
        val remoteUrl: String,
        override val displayName: String = "Link",
        override val isUploading: Boolean = false
    ) : AttachmentUiModel {
        override val type = MediaType.LINK
    }
}

sealed interface RecordingUiState {
    data object Idle : RecordingUiState
    data class Recording(
        val elapsedFormatted: String,
        val amplitudes: ImmutableList<Float>
    ) : RecordingUiState
}

data class LocationInfoUiModel(
    val coordinates: Pair<Double, Double>,
    val displayName: String? = null
)