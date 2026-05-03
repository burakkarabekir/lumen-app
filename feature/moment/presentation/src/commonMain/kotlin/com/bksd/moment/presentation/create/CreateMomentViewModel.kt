@file:OptIn(ExperimentalUuidApi::class)

package com.bksd.moment.presentation.create

import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.LocationErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.location.LocationProvider
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.DraftAudio
import com.bksd.core.domain.model.DraftLink
import com.bksd.core.domain.model.DraftPhoto
import com.bksd.core.domain.model.DraftVideo
import com.bksd.core.domain.model.MediaType
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.repository.MediaRepository
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.VoiceRecorder
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.core.presentation.util.toFormattedTime
import com.bksd.journal.domain.model.Moment
import com.bksd.moment.domain.usecase.SaveMomentUseCase
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.create.mappers.toLocationData
import com.bksd.moment.presentation.create.mappers.toLocationInfoUiModel
import com.bksd.moment.presentation.error_attachment_save_failed
import com.bksd.moment.presentation.error_audio_permission_required
import com.bksd.moment.presentation.error_camera_permission_denied
import com.bksd.moment.presentation.error_file_too_large
import com.bksd.moment.presentation.error_location_fetch_failed
import com.bksd.moment.presentation.error_location_permission_denied
import com.bksd.moment.presentation.error_location_services_disabled
import com.bksd.moment.presentation.error_moment_empty
import com.bksd.moment.presentation.error_moment_save_failed
import com.bksd.moment.presentation.error_mood_required
import com.bksd.moment.presentation.error_playback_failed
import com.bksd.moment.presentation.error_recording_save_failed
import com.bksd.moment.presentation.error_recording_start_failed
import com.bksd.moment.presentation.success_moment_saved
import com.bksd.moment.presentation.timestamp_today_format
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.getString
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CreateMomentViewModel(
    private val saveMomentUseCase: SaveMomentUseCase,
    private val mediaRepository: MediaRepository,
    private val voiceRecorder: VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val locationProvider: LocationProvider,
    private val authRepository: AuthRepository
) : BaseViewModel<CreateMomentAction, CreateMomentEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CreateMomentState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                initState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateMomentState()
        )

    fun initState() {
        val now = Clock.System.now()
        launch {
            val formatted = getString(Res.string.timestamp_today_format, now.toFormattedTime())
            updateState { it.copy(timestampFormatted = formatted) }
        }

        launch { voiceRecorder.isRecording.collect { updateRecordingState() } }
        launch { voiceRecorder.elapsedMs.collect { updateRecordingState() } }
        launch { voiceRecorder.amplitudes.collect { updateRecordingState() } }
        launch {
            audioPlayer.playbackState.collect { playbackState ->
                updateState { it.copy(playbackState = playbackState) }
            }
        }
        launch {
            audioPlayer.playbackAmplitudes.collect { amplitudes ->
                updateState { it.copy(playbackAmplitudes = amplitudes.toImmutableList()) }
            }
        }
        launch {
            audioPlayer.currentPositionMs.collect { positionMs ->
                updateState { it.copy(playbackPositionFormatted = formatMs(positionMs)) }
            }
        }
        launch {
            audioPlayer.durationMs.collect { durationMs ->
                updateState { it.copy(playbackDurationFormatted = formatMs(durationMs)) }
            }
        }
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

    private fun handleAddLocationClick() {
        if (locationProvider.hasPermission()) {
            fetchLocation()
        } else {
            sendEvent(CreateMomentEvent.RequestLocationPermission)
        }
    }

    private fun fetchLocation() {
        updateState { it.copy(isFetchingLocation = true) }
        launch {
            when (val result = locationProvider.getCurrentLocation()) {
                is Result.Success -> {
                    updateState {
                        it.copy(
                            location = result.data.toLocationInfoUiModel(),
                            isFetchingLocation = false
                        )
                    }
                }

                is Result.Error -> {
                    updateState { it.copy(isFetchingLocation = false) }
                    val errorText = when (val error = result.error) {
                        is AppError.Location -> when (error.type) {
                            LocationErrorType.SERVICES_DISABLED -> UiText.Resource(Res.string.error_location_services_disabled)
                            LocationErrorType.PERMISSION_DENIED -> UiText.Resource(Res.string.error_location_permission_denied)
                            LocationErrorType.UNAVAILABLE -> UiText.Resource(Res.string.error_location_fetch_failed)
                        }

                        else -> UiText.Resource(Res.string.error_location_fetch_failed)
                    }
                    sendEvent(CreateMomentEvent.ShowError(errorText))
                }
            }
        }
    }

    private fun updateState(transform: (CreateMomentState) -> CreateMomentState) {
        _state.update(transform)
    }

    override fun onAction(action: CreateMomentAction) {
        when (action) {
            is CreateMomentAction.OnBodyChange -> updateState { it.copy(body = action.body) }
            is CreateMomentAction.OnMoodSelect -> updateState { it.copy(selectedMood = action.mood) }
            is CreateMomentAction.OnTagAdd -> {
                if (action.tag.isNotBlank() && !state.value.tags.contains(action.tag)) {
                    updateState { it.copy(tags = (it.tags + action.tag.trim()).toPersistentList()) }
                }
            }

            is CreateMomentAction.OnTagRemove -> updateState { it.copy(tags = (it.tags - action.tag).toPersistentList()) }
            is CreateMomentAction.OnDateSelect -> updateState { it.copy(selectedDate = action.date) }
            CreateMomentAction.OnBackClick -> sendEvent(CreateMomentEvent.NavigateBack)
            CreateMomentAction.OnSaveClick -> saveMoment()

            // Attachments
            is CreateMomentAction.OnRemoveAttachment -> removeAttachment(action.id)
            CreateMomentAction.OnToggleAttachments -> {
                updateState { it.copy(isAttachmentsExpanded = !it.isAttachmentsExpanded) }
            }

            // Location
            CreateMomentAction.OnAddLocationClick -> handleAddLocationClick()
            CreateMomentAction.OnRemoveLocationClick -> updateState { it.copy(location = null) }

            CreateMomentAction.OnLocationPermissionGranted -> fetchLocation()
            CreateMomentAction.OnLocationPermissionDenied -> {
                sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_location_permission_denied)))
            }

            // Media
            CreateMomentAction.OnCameraClick -> sendEvent(CreateMomentEvent.RequestCameraPermission)
            CreateMomentAction.OnCameraPermissionGranted -> sendEvent(CreateMomentEvent.LaunchCamera)
            CreateMomentAction.OnCameraPermissionDenied -> {
                sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_camera_permission_denied)))
            }
            CreateMomentAction.OnPhotoClick -> sendEvent(CreateMomentEvent.LaunchPhotoPicker)
            CreateMomentAction.OnVideoClick -> sendEvent(CreateMomentEvent.LaunchVideoPicker)
            CreateMomentAction.OnFilePickClick -> sendEvent(CreateMomentEvent.LaunchFilePicker)
            is CreateMomentAction.OnMediaPicked -> handleMediaPicked(action)
            CreateMomentAction.OnMicClick -> sendEvent(CreateMomentEvent.RequestAudioPermission)

            CreateMomentAction.OnAudioPermissionGranted -> {
                launch {
                    audioPlayer.stop()
                    updateState { it.copy(isRecordingSheetVisible = true) }
                    val result = voiceRecorder.startRecording()
                    if (result is Result.Error) {
                        updateState { it.copy(isRecordingSheetVisible = false) }
                        sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_recording_start_failed)))
                    }
                }
            }

            CreateMomentAction.OnAudioPermissionDenied -> {
                sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_audio_permission_required)))
            }

            CreateMomentAction.OnStopRecording -> {
                launch {
                    voiceRecorder.stopRecording()
                    updateState { it.copy(isRecordingSheetVisible = false) }
                }
            }

            CreateMomentAction.OnRecordingDone -> {
                launch {
                    val result = voiceRecorder.stopRecording()
                    if (result is Result.Success) {
                        val newAttachment = AttachmentUiModel.Audio(
                            id = Uuid.random().toString(),
                            localPath = result.data,
                            sizeBytes = 0L // TODO: Get size if needed
                        )
                        updateState {
                            it.copy(
                                audioAttachment = newAttachment,
                                isRecordingSheetVisible = false
                            )
                        }
                    } else {
                        updateState { it.copy(isRecordingSheetVisible = false) }
                        sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_recording_save_failed)))
                    }
                }
            }

            CreateMomentAction.OnCancelRecording -> {
                launch {
                    voiceRecorder.cancelRecording()
                    updateState { it.copy(isRecordingSheetVisible = false) }
                }
            }

            CreateMomentAction.OnDismissRecordingSheet -> {
                launch {
                    if (voiceRecorder.isRecording.value) {
                        voiceRecorder.cancelRecording()
                    }
                    updateState { it.copy(isRecordingSheetVisible = false) }
                }
            }

            is CreateMomentAction.OnPlayAudio -> {
                val filePath = state.value.audioAttachment?.localPath
                if (filePath != null) {
                    launch {
                        if (audioPlayer.playbackState.value == PlaybackState.PAUSED) {
                            audioPlayer.resume()
                        } else {
                            val result = audioPlayer.play(filePath)
                            if (result is Result.Error) {
                                sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_playback_failed)))
                            }
                        }
                    }
                }
            }

            CreateMomentAction.OnPauseAudio -> {
                launch { audioPlayer.pause() }
            }

            is CreateMomentAction.OnDeleteRecording -> {
                launch {
                    audioPlayer.stop()
                    updateState { it.copy(audioAttachment = null) }
                }
            }

            CreateMomentAction.OnLinkClick -> {
                updateState { it.copy(isLinkSheetVisible = true) }
            }

            CreateMomentAction.OnDismissLinkSheet -> {
                updateState { it.copy(isLinkSheetVisible = false, linkInput = "") }
            }

            is CreateMomentAction.OnLinkInputChange -> updateState { it.copy(linkInput = action.text) }
            CreateMomentAction.OnAddLink -> {
                val linkUrl = state.value.linkInput.trim()
                if (linkUrl.isNotEmpty()) {
                    val newAttachment = AttachmentUiModel.Link(
                        id = Uuid.random().toString(),
                        remoteUrl = linkUrl
                    )
                    updateState {
                        it.copy(
                            linkAttachment = newAttachment,
                            linkInput = "",
                            isLinkSheetVisible = false
                        )
                    }
                }
            }

            is CreateMomentAction.OnRemoveLink -> {
                updateState { it.copy(linkAttachment = null) }
            }
        }
    }

    private fun handleMediaPicked(action: CreateMomentAction.OnMediaPicked) {
        val maxSize = 25 * 1024 * 1024 // 25 MB
        if (action.sizeBytes > maxSize) {
            sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_file_too_large)))
            return
        }

        val newAttachment = when (action.type) {
            MediaType.PHOTO -> AttachmentUiModel.Photo(
                id = Uuid.random().toString(),
                localPath = action.path,
                sizeBytes = action.sizeBytes
            )

            MediaType.VIDEO -> AttachmentUiModel.Video(
                id = Uuid.random().toString(),
                localPath = action.path,
                sizeBytes = action.sizeBytes
            )

            else -> return // Pickers only return Photo/Video
        }

        updateState {
            when (newAttachment) {
                is AttachmentUiModel.Photo -> it.copy(photoAttachment = newAttachment)
                is AttachmentUiModel.Video -> it.copy(videoAttachment = newAttachment)
                else -> it
            }
        }
    }

    private fun removeAttachment(id: String) {
        updateState { state ->
            state.copy(
                photoAttachment = if (state.photoAttachment?.id == id) null else state.photoAttachment,
                videoAttachment = if (state.videoAttachment?.id == id) null else state.videoAttachment,
                audioAttachment = if (state.audioAttachment?.id == id) null else state.audioAttachment,
                linkAttachment = if (state.linkAttachment?.id == id) null else state.linkAttachment
            )
        }
    }

    private fun saveMoment() {
        val currentState = state.value

        val attachmentsToSave = currentState.allAttachments

        if (currentState.body.isBlank() && attachmentsToSave.isEmpty()) {
            sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_moment_empty)))
            return
        }
        if (currentState.selectedMood == null) {
            sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_mood_required)))
            return
        }

        updateState { it.copy(isSaving = true) }

        launch {
            if (audioPlayer.playbackState.value != PlaybackState.STOPPED) {
                audioPlayer.stop()
            }

            val userId = authRepository.getSignedInUserId()
            if (userId == null) {
                updateState { it.copy(isSaving = false) }
                sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_moment_save_failed)))
                return@launch
            }
            val momentId = Uuid.random().toString()

            val uploadedAttachments = mutableListOf<Attachment>()

            for (uiModel in attachmentsToSave) {
                val draft = when (uiModel) {
                    is AttachmentUiModel.Photo -> DraftPhoto(
                        AttachmentId(uiModel.id),
                        uiModel.localPath ?: uiModel.remoteUrl.orEmpty(),
                        uiModel.sizeBytes
                    )

                    is AttachmentUiModel.Video -> DraftVideo(
                        AttachmentId(uiModel.id),
                        uiModel.localPath ?: uiModel.remoteUrl.orEmpty(),
                        uiModel.durationMs,
                        uiModel.sizeBytes
                    )

                    is AttachmentUiModel.Audio -> DraftAudio(
                        AttachmentId(uiModel.id),
                        uiModel.localPath ?: uiModel.remoteUrl.orEmpty(),
                        uiModel.durationMs,
                        uiModel.sizeBytes
                    )

                    is AttachmentUiModel.Link -> DraftLink(
                        AttachmentId(uiModel.id),
                        Url(uiModel.remoteUrl)
                    )
                }

                when (val result = mediaRepository.uploadAttachment(draft, userId, momentId)) {
                    is Result.Success -> uploadedAttachments.add(result.data)
                    is Result.Error -> {
                        updateState { it.copy(isSaving = false) }
                        sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_attachment_save_failed)))
                        return@launch
                    }
                }
            }

            val newMoment = Moment(
                id = momentId,
                body = currentState.body.takeIf { it.isNotBlank() },
                createdAt = Clock.System.now(),
                mood = currentState.selectedMood,
                tags = currentState.tags.toList(),
                attachments = uploadedAttachments.toList(),
                location = currentState.location?.toLocationData()
            )

            val result = saveMomentUseCase(newMoment)

            updateState { it.copy(isSaving = false) }

            when (result) {
                is Result.Error -> {
                    sendEvent(CreateMomentEvent.ShowError(UiText.Resource(Res.string.error_moment_save_failed)))
                }

                is Result.Success -> {
                    sendEvent(CreateMomentEvent.ShowSaveSuccess(UiText.Resource(Res.string.success_moment_saved)))
                    sendEvent(CreateMomentEvent.NavigateBack)
                }
            }
        }
    }
}
