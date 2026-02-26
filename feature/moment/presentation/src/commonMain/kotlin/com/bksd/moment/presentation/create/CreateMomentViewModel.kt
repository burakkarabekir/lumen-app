@file:OptIn(ExperimentalUuidApi::class)

package com.bksd.moment.presentation.create

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.location.LocationProvider
import com.bksd.core.domain.model.MediaAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.repository.MediaRepository
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.VoiceRecorder
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment
import com.bksd.moment.domain.usecase.SaveMomentUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CreateMomentViewModel(
    private val saveMomentUseCase: SaveMomentUseCase,
    private val mediaRepository: MediaRepository,
    private val voiceRecorder: VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val locationProvider: LocationProvider
) : BaseViewModel<CreateMomentAction, CreateMomentEvent>() {

    private val _stateFlow = MutableStateFlow(CreateMomentState())
    val state: StateFlow<CreateMomentState> = _stateFlow.asStateFlow()

    init {
        // Collect voice recorder states
        viewModelScope.launch {
            voiceRecorder.isRecording.collect { updateRecordingState() }
        }
        viewModelScope.launch {
            voiceRecorder.elapsedMs.collect { updateRecordingState() }
        }
        viewModelScope.launch {
            voiceRecorder.amplitudes.collect { updateRecordingState() }
        }

        // Collect audio player states
        viewModelScope.launch {
            audioPlayer.playbackState.collect { playbackState ->
                updateState { it.copy(playbackState = playbackState) }
            }
        }
        viewModelScope.launch {
            audioPlayer.playbackAmplitudes.collect { amplitudes ->
                updateState { it.copy(playbackAmplitudes = amplitudes.toImmutableList()) }
            }
        }
        viewModelScope.launch {
            audioPlayer.currentPositionMs.collect { positionMs ->
                updateState { it.copy(playbackPositionFormatted = formatMs(positionMs)) }
            }
        }
        viewModelScope.launch {
            audioPlayer.durationMs.collect { durationMs ->
                updateState { it.copy(playbackDurationFormatted = formatMs(durationMs)) }
            }
        }

        fetchLocation()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    private fun updateRecordingState() {
        val isRecording = voiceRecorder.isRecording.value
        val elapsed = voiceRecorder.elapsedMs.value
        val amps = voiceRecorder.amplitudes.value.toImmutableList()

        if (isRecording) {
            val seconds = (elapsed / 1000) % 60
            val minutes = (elapsed / 60000) % 60
            val formatted =
                "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

            updateState {
                it.copy(recordingState = RecordingUiState.Recording(formatted, amps))
            }
        } else {
            updateState { it.copy(recordingState = RecordingUiState.Idle) }
        }
    }

    private fun formatMs(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "$minutes:${seconds.toString().padStart(2, '0')}"
    }

    private fun fetchLocation() {
        if (!locationProvider.hasPermission()) return

        viewModelScope.launch {
            when (val result = locationProvider.getCurrentLocation()) {
                is Result.Success -> {
                    updateState { it.copy(location = result.data.displayName) }
                }

                is Result.Error -> { /* Ignore silently */
                }
            }
        }
    }

    private fun updateState(transform: (CreateMomentState) -> CreateMomentState) {
        _stateFlow.update(transform)
    }

    override fun onAction(action: CreateMomentAction) {
        when (action) {
            is CreateMomentAction.OnBodyChange -> updateState { it.copy(body = action.body) }
            is CreateMomentAction.OnMoodSelect -> updateState { it.copy(selectedMood = action.mood) }
            is CreateMomentAction.OnTagAdd -> {
                if (action.tag.isNotBlank() && !state.value.tags.contains(action.tag)) {
                    updateState { it.copy(tags = it.tags + action.tag.trim()) }
                }
            }

            is CreateMomentAction.OnTagRemove -> updateState { it.copy(tags = it.tags - action.tag) }
            is CreateMomentAction.OnDateSelect -> updateState { it.copy(selectedDate = action.date) }
            CreateMomentAction.OnBackClick -> sendEvent(CreateMomentEvent.NavigateBack)
            CreateMomentAction.OnSaveClick -> saveMoment()

            // Media Launchers
            CreateMomentAction.OnCameraClick -> sendEvent(CreateMomentEvent.LaunchCamera)
            CreateMomentAction.OnVideoClick -> sendEvent(CreateMomentEvent.LaunchVideoPicker)
            CreateMomentAction.OnFilePickClick -> sendEvent(CreateMomentEvent.LaunchFilePicker)

            // Media Handlers
            is CreateMomentAction.OnMediaPicked -> handleMediaPicked(action)
            is CreateMomentAction.OnRemoveAttachment -> removeAttachment(action.id)
            CreateMomentAction.OnToggleAttachments -> {
                updateState { it.copy(isAttachmentsExpanded = !it.isAttachmentsExpanded) }
            }

            // Voice Recording — Permission → Bottom Sheet Flow
            CreateMomentAction.OnMicClick -> {
                // Request permission first; Screen handles the permission flow
                sendEvent(CreateMomentEvent.RequestAudioPermission)
            }

            CreateMomentAction.OnAudioPermissionGranted -> {
                // Permission granted — show bottom sheet and start recording
                viewModelScope.launch {
                    audioPlayer.stop()
                    updateState { it.copy(isRecordingSheetVisible = true) }
                    val result = voiceRecorder.startRecording()
                    if (result is Result.Error) {
                        updateState { it.copy(isRecordingSheetVisible = false) }
                        sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("Failed to start recording")))
                    }
                }
            }

            CreateMomentAction.OnAudioPermissionDenied -> {
                sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("Microphone permission is required to record audio")))
            }

            CreateMomentAction.OnStopRecording -> {
                viewModelScope.launch {
                    voiceRecorder.stopRecording()
                    updateState { it.copy(isRecordingSheetVisible = false) }
                }
            }

            CreateMomentAction.OnRecordingDone -> {
                // Stop recording, save as attachment, dismiss sheet
                viewModelScope.launch {
                    val result = voiceRecorder.stopRecording()
                    if (result is Result.Success) {
                        val newAttachment = AttachmentUiModel(
                            id = Uuid.random().toString(),
                            type = com.bksd.core.domain.model.MediaType.AUDIO,
                            localPath = result.data,
                            displayName = "Voice Note"
                        )
                        updateState {
                            it.copy(
                                attachments = (it.attachments + newAttachment).toImmutableList(),
                                isAttachmentsExpanded = true,
                                isRecordingSheetVisible = false
                            )
                        }
                    } else {
                        updateState { it.copy(isRecordingSheetVisible = false) }
                        sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("Failed to save recording")))
                    }
                }
            }

            CreateMomentAction.OnCancelRecording -> {
                viewModelScope.launch {
                    voiceRecorder.cancelRecording()
                    updateState { it.copy(isRecordingSheetVisible = false) }
                }
            }

            CreateMomentAction.OnDismissRecordingSheet -> {
                // Dismiss triggered by swipe/back — cancel recording
                viewModelScope.launch {
                    if (voiceRecorder.isRecording.value) {
                        voiceRecorder.cancelRecording()
                    }
                    updateState { it.copy(isRecordingSheetVisible = false) }
                }
            }

            // Audio Playback
            is CreateMomentAction.OnPlayAudio -> {
                val attachment = state.value.attachments.find { it.id == action.attachmentId }
                val filePath = attachment?.localPath
                if (filePath != null) {
                    viewModelScope.launch {
                        if (audioPlayer.playbackState.value == PlaybackState.PAUSED) {
                            audioPlayer.resume()
                        } else {
                            val result = audioPlayer.play(filePath)
                            if (result is Result.Error) {
                                sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("Failed to play recording")))
                            }
                        }
                    }
                }
            }

            CreateMomentAction.OnPauseAudio -> {
                viewModelScope.launch { audioPlayer.pause() }
            }

            is CreateMomentAction.OnDeleteRecording -> {
                viewModelScope.launch {
                    audioPlayer.stop()
                    removeAttachment(action.attachmentId)
                }
            }

            // Link Actions
            CreateMomentAction.OnLinkClick -> {
                updateState { it.copy(isLinkSheetVisible = true) }
            }

            CreateMomentAction.OnDismissLinkSheet -> {
                updateState { it.copy(isLinkSheetVisible = false, linkInput = "") }
            }

            is CreateMomentAction.OnLinkInputChange -> updateState { it.copy(linkInput = action.text) }
            CreateMomentAction.OnAddLink -> {
                val link = state.value.linkInput.trim()
                if (link.isNotEmpty() && !state.value.links.contains(link)) {
                    updateState {
                        it.copy(
                            links = (it.links + link).toImmutableList(),
                            linkInput = "",
                            isLinkSheetVisible = false,
                            isAttachmentsExpanded = true
                        )
                    }
                }
            }

            is CreateMomentAction.OnRemoveLink -> {
                updateState { it.copy(links = (it.links - action.url).toImmutableList()) }
            }
        }
    }

    private fun handleMediaPicked(action: CreateMomentAction.OnMediaPicked) {
        val maxSize = 25 * 1024 * 1024 // 25 MB
        if (action.sizeBytes > maxSize) {
            sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("File exceeds 25 MB limit")))
            return
        }

        val newAttachment = AttachmentUiModel(
            id = Uuid.random().toString(),
            type = action.type,
            localPath = action.path,
            displayName = "Attached Media"
        )
        updateState {
            it.copy(
                attachments = (it.attachments + newAttachment).toImmutableList(),
                isAttachmentsExpanded = true
            )
        }
    }

    private fun removeAttachment(id: String) {
        updateState { state ->
            val newAttachments = state.attachments.filter { it.id != id }.toImmutableList()
            state.copy(
                attachments = newAttachments,
                isAttachmentsExpanded = newAttachments.isNotEmpty() && state.isAttachmentsExpanded
            )
        }
    }

    private fun saveMoment() {
        val currentState = state.value

        if (currentState.body.isBlank() && currentState.attachments.isEmpty() && currentState.links.isEmpty()) {
            sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("Moment is empty")))
            return
        }
        if (currentState.selectedMood == null) {
            sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("Please select a mood")))
            return
        }

        updateState { it.copy(isSaving = true) }

        viewModelScope.launch {
            // Stop any ongoing playback before saving
            audioPlayer.stop()

            val dummyId = "moment_${Random.nextInt(10000, 99999)}"
            val dummyUserId = "user_123"
            val now = 1739462400000L

            val uploadedAttachments = currentState.attachments.map { uiModel ->
                val domainModel = MediaAttachment(
                    id = uiModel.id,
                    type = uiModel.type,
                    localPath = uiModel.localPath
                )

                if (domainModel.localPath != null) {
                    when (val result =
                        mediaRepository.uploadAttachment(domainModel, dummyUserId, dummyId)) {
                        is Result.Success -> result.data
                        is Result.Error -> domainModel
                    }
                } else domainModel
            }

            val newMoment = Moment(
                id = dummyId,
                body = currentState.body.takeIf { it.isNotBlank() },
                timestamp = now,
                mood = currentState.selectedMood,
                tags = currentState.tags,
                attachments = uploadedAttachments,
                links = currentState.links,
                location = currentState.location?.let {
                    com.bksd.core.domain.location.LocationData(0.0, 0.0, it)
                }
            )

            val result = saveMomentUseCase(newMoment)

            updateState { it.copy(isSaving = false) }

            when (result) {
                is Result.Error -> {
                    sendEvent(CreateMomentEvent.ShowError(UiText.Dynamic("Failed to save moment")))
                }

                is Result.Success -> {
                    sendEvent(CreateMomentEvent.ShowSaveSuccess(UiText.Dynamic("Moment saved!")))
                    sendEvent(CreateMomentEvent.NavigateBack)
                }
            }
        }
    }
}
