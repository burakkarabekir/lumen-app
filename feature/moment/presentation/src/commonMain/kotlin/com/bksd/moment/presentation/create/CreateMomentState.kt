package com.bksd.moment.presentation.create

import com.bksd.core.domain.model.MediaType
import com.bksd.core.domain.model.PlaybackState
import com.bksd.journal.domain.model.Mood
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

data class CreateMomentState(
    val body: String = "",
    val selectedMood: Mood? = null,
    val tags: List<String> = emptyList(),
    val isSaving: Boolean = false,
    val selectedDate: LocalDate? = null,

    // Media capabilities
    val attachments: ImmutableList<AttachmentUiModel> = persistentListOf(),
    val isAttachmentsExpanded: Boolean = false,
    val recordingState: RecordingUiState = RecordingUiState.Idle,
    val isRecordingSheetVisible: Boolean = false,
    val location: String? = null,
    val timestampFormatted: String = "Today",
    val links: ImmutableList<String> = persistentListOf(),
    val linkInput: String = "",
    val isLinkSheetVisible: Boolean = false,

    // Audio playback
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val playbackAmplitudes: ImmutableList<Float> = persistentListOf(),
    val playbackPositionFormatted: String = "0:00",
    val playbackDurationFormatted: String = "0:00"
)

data class AttachmentUiModel(
    val id: String,
    val type: MediaType,
    val localPath: String? = null,
    val remoteUrl: String? = null,
    val displayName: String = "",
    val isUploading: Boolean = false
)

sealed interface RecordingUiState {
    data object Idle : RecordingUiState
    data class Recording(
        val elapsedFormatted: String,
        val amplitudes: ImmutableList<Float>
    ) : RecordingUiState
}
