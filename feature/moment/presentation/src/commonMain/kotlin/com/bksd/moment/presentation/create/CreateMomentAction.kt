package com.bksd.moment.presentation.create

import com.bksd.core.domain.model.MediaType
import com.bksd.journal.domain.model.Mood
import kotlinx.datetime.LocalDate

sealed interface CreateMomentAction {
    data class OnBodyChange(val body: String) : CreateMomentAction
    data class OnMoodSelect(val mood: Mood) : CreateMomentAction
    data class OnTagAdd(val tag: String) : CreateMomentAction
    data class OnTagRemove(val tag: String) : CreateMomentAction
    data class OnDateSelect(val date: LocalDate) : CreateMomentAction
    data object OnSaveClick : CreateMomentAction
    data object OnBackClick : CreateMomentAction

    // Media & Location Actions
    data object OnMicClick : CreateMomentAction
    data object OnStopRecording : CreateMomentAction
    data object OnRecordingDone : CreateMomentAction
    data object OnCancelRecording : CreateMomentAction
    data object OnDismissRecordingSheet : CreateMomentAction
    data object OnAudioPermissionGranted : CreateMomentAction
    data object OnAudioPermissionDenied : CreateMomentAction
    data object OnCameraClick : CreateMomentAction
    data object OnVideoClick : CreateMomentAction
    data object OnFilePickClick : CreateMomentAction
    data class OnMediaPicked(val path: String, val type: MediaType, val sizeBytes: Long) :
        CreateMomentAction

    data class OnRemoveAttachment(val id: String) : CreateMomentAction
    data object OnToggleAttachments : CreateMomentAction

    // Audio Playback Actions
    data class OnPlayAudio(val attachmentId: String) : CreateMomentAction
    data object OnPauseAudio : CreateMomentAction
    data class OnDeleteRecording(val attachmentId: String) : CreateMomentAction

    // Link Actions
    data object OnLinkClick : CreateMomentAction
    data object OnDismissLinkSheet : CreateMomentAction
    data class OnLinkInputChange(val text: String) : CreateMomentAction
    data object OnAddLink : CreateMomentAction
    data class OnRemoveLink(val url: String) : CreateMomentAction
}
