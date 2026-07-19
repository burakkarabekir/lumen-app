package com.bksd.journal.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.connectivity.NetworkMonitor
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.presentation.labelRes
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.core.presentation.util.toUiText
import org.jetbrains.compose.resources.getString
import com.bksd.journal.domain.usecase.DeleteMomentUseCase
import com.bksd.journal.domain.usecase.GetMomentUseCase
import com.bksd.journal.domain.usecase.UpdateMomentUseCase
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.detail_changes_saved
import com.bksd.journal.presentation.error_mood_limit
import com.bksd.journal.presentation.detail_moment_deleted
import com.bksd.reflection.domain.model.MomentAnalysisState
import com.bksd.reflection.domain.usecase.ObserveEntryAnalysisUseCase
import com.bksd.reflection.domain.usecase.RequestEntryAnalysisUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Clock

private const val MAX_MOODS = 5

class MomentDetailViewModel(
    private val getMomentUseCase: GetMomentUseCase,
    private val deleteMomentUseCase: DeleteMomentUseCase,
    private val updateMomentUseCase: UpdateMomentUseCase,
    private val observeEntryAnalysis: ObserveEntryAnalysisUseCase,
    private val requestEntryAnalysis: RequestEntryAnalysisUseCase,
    private val audioPlayer: AudioPlayer,
    private val networkMonitor: NetworkMonitor,
    private val momentId: String,
    private val initialIsEditing: Boolean = false
) : BaseViewModel<MomentDetailAction, MomentDetailEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MomentDetailState(isLoading = true))

    val state: StateFlow<MomentDetailState> = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMoment()
                observeAudio()
                observeAnalysis()
                observeConnectivity()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    override fun onAction(action: MomentDetailAction) {
        when (action) {
            MomentDetailAction.OnNavigateBack -> handleNavigateBack()
            MomentDetailAction.OnMenuClick -> Unit
            MomentDetailAction.OnEditClick -> enterEditMode()
            MomentDetailAction.OnSaveChanges -> handleSaveChanges()
            MomentDetailAction.OnCancelEdit -> exitEditMode()
            MomentDetailAction.OnDeleteClick -> _state.update { it.copy(showDeleteDialog = true) }
            MomentDetailAction.OnConfirmDelete -> handleDelete()
            MomentDetailAction.OnDismissDelete -> _state.update { it.copy(showDeleteDialog = false) }
            MomentDetailAction.OnShareClick -> handleShare()
            MomentDetailAction.OnUpgradeClick -> sendEvent(MomentDetailEvent.NavigateToPaywall)
            MomentDetailAction.OnRetryAnalysis -> handleRetryAnalysis()
            MomentDetailAction.OnFavoriteToggle -> toggleFavorite()
            MomentDetailAction.OnToggleBodyExpand -> {
                _state.update { it.copy(isBodyExpanded = !it.isBodyExpanded) }
            }
            is MomentDetailAction.OnTitleChange -> {
                _state.update { it.copy(editTitle = action.title) }
            }
            is MomentDetailAction.OnBodyChange -> {
                _state.update { it.copy(editBody = action.body) }
            }
            is MomentDetailAction.OnMoodToggle -> toggleMood(action.mood)
            is MomentDetailAction.OnTagRemove -> {
                _state.update { it.copy(editTags = (it.editTags - action.tag).toPersistentList()) }
            }
            is MomentDetailAction.OnAudioPlayClick -> playAudio(action.url)
            MomentDetailAction.OnAudioPauseClick -> launch { audioPlayer.pause() }
            MomentDetailAction.OnLocationRemove -> _state.update { it.copy(editLocation = null) }
            is MomentDetailAction.OnDateChange -> _state.update { it.copy(editCreatedAt = action.date) }
            is MomentDetailAction.OnAttachmentRemove -> _state.update {
                it.copy(
                    editAttachments = it.editAttachments
                        .filter { att -> att.id != action.id }
                        .toPersistentList()
                )
            }
        }
    }

    private fun handleShare() {
        if (_state.value.moment == null) return
        sendEvent(MomentDetailEvent.ShowShareSheet)
    }

    private fun playAudio(url: String) {
        _state.update { it.copy(playingAudioUrl = url) }
        launch {
            if (audioPlayer.playbackState.value == PlaybackState.PAUSED) {
                audioPlayer.resume()
            } else {
                audioPlayer.play(url)
            }
        }
    }

    private fun observeAudio() {
        launch {
            audioPlayer.playbackState.collect { st ->
                _state.update { it.copy(audioPlaybackState = st) }
            }
        }
        launch {
            audioPlayer.durationMs.collect { ms ->
                _state.update { it.copy(audioDurationFormatted = formatMs(ms)) }
            }
        }
    }

    private var reconciledPending = false

    private fun observeAnalysis() {
        launch {
            observeEntryAnalysis(momentId).collect { analysisState ->
                _state.update { it.copy(analysis = analysisState) }
                if (analysisState == MomentAnalysisState.Pending) {
                    reconcileStalePending()
                }
            }
        }
    }

    private fun observeConnectivity() {
        launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .drop(1)
                .filter { it }
                .collect {
                    val analysis = _state.value.analysis
                    if (analysis == MomentAnalysisState.Failed ||
                        analysis == MomentAnalysisState.Offline
                    ) {
                        handleRetryAnalysis()
                    }
                }
        }
    }

    private fun formatMs(ms: Long): String {
        val total = ms / 1000
        val minutes = total / 60
        val seconds = total % 60
        return "$minutes:${seconds.toString().padStart(2, '0')}"
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    private companion object {
        const val STALE_PENDING_MS = 120_000L
    }

    private fun toggleFavorite() {
        val moment = _state.value.moment ?: return
        val updated = moment.copy(isFavorite = !moment.isFavorite)
        _state.update { it.copy(moment = updated) }
        launch { updateMomentUseCase(updated) }
    }

    private fun handleNavigateBack() {
        if (_state.value.isEditing) {
            exitEditMode()
        } else {
            sendEvent(MomentDetailEvent.NavigateBack)
        }
    }

    private fun enterEditMode() {
        val moment = _state.value.moment ?: return
        _state.update {
            it.copy(
                isEditing = true,
                editTitle = moment.title,
                editBody = moment.body.orEmpty(),
                editMoods = moment.moods.toSet().toPersistentSet(),
                editTags = moment.tags.toPersistentList(),
                editLocation = moment.location,
                editCreatedAt = moment.createdAt,
                editAttachments = moment.attachments.toPersistentList()
            )
        }
    }

    private fun exitEditMode() {
        _state.update { it.copy(isEditing = false) }
    }

    private fun handleSaveChanges() {
        val current = _state.value
        val moment = current.moment ?: return

        _state.update { it.copy(isSaving = true) }

        val updated = moment.copy(
            title = current.editTitle,
            body = current.editBody.ifEmpty { null },
            moods = current.editMoods.toList(),
            tags = current.editTags.toList(),
            location = current.editLocation,
            createdAt = current.editCreatedAt ?: moment.createdAt,
            attachments = current.editAttachments.toList()
        )

        launch {
            when (val result = updateMomentUseCase(updated)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            moment = updated,
                            isEditing = false,
                            isSaving = false
                        )
                    }
                    sendEvent(MomentDetailEvent.ShowSuccess(UiText.Resource(Res.string.detail_changes_saved)))
                }

                is Result.Error -> {
                    _state.update { it.copy(isSaving = false) }
                    sendEvent(
                        MomentDetailEvent.ShowError(
                            result.error.toUiText()
                        )
                    )
                }
            }
        }
    }

    private fun toggleMood(mood: com.bksd.core.domain.model.Mood) {
        val selected = _state.value.editMoods
        if (mood !in selected && selected.size >= MAX_MOODS) {
            sendEvent(MomentDetailEvent.ShowError(UiText.Resource(Res.string.error_mood_limit)))
            return
        }
        _state.update { current ->
            val updated = if (mood in current.editMoods) {
                current.editMoods - mood
            } else {
                current.editMoods + mood
            }
            current.copy(editMoods = updated.toPersistentSet())
        }
    }

    private fun loadMoment() {
        _state.update { it.copy(isLoading = true, error = null) }
        launch {
            when (val result = getMomentUseCase(momentId)) {
                is Result.Error -> {
                    val errorText = result.error.toUiText()
                    _state.update { it.copy(error = errorText, isLoading = false) }
                    sendEvent(MomentDetailEvent.ShowError(errorText))
                }

                is Result.Success -> {
                    _state.update { it.copy(moment = result.data, isLoading = false) }
                    if (initialIsEditing) {
                        enterEditMode()
                    }
                    reconcileStalePending()
                }
            }
        }
    }

    private fun handleDelete() {
        _state.update { it.copy(isDeleting = true, showDeleteDialog = false) }
        launch {
            when (val result = deleteMomentUseCase(momentId)) {
                is Result.Success -> {
                    sendEvent(MomentDetailEvent.ShowSuccess(UiText.Resource(Res.string.detail_moment_deleted)))
                    sendEvent(MomentDetailEvent.NavigateBack)
                }

                is Result.Error -> {
                    _state.update { it.copy(isDeleting = false, showDeleteDialog = false) }
                    val errorText = result.error.toUiText()
                    sendEvent(MomentDetailEvent.ShowError(errorText))
                }
            }
        }
    }

    private fun handleRetryAnalysis() {
        if (_state.value.analysis == MomentAnalysisState.Pending) return
        runAnalysis()
    }

    private fun runAnalysis() {
        val moment = _state.value.moment ?: return

        val entryText = listOfNotNull(
            moment.title.trim().takeIf { it.isNotBlank() },
            moment.body?.trim()?.takeIf { it.isNotBlank() }
        ).joinToString("\n\n")
        if (entryText.isBlank()) return

        launch {
            val moods = moment.moods.map { getString(it.labelRes()) }
            requestEntryAnalysis(moment.id, entryText, moods)
        }
    }

    /**
     * Re-drives an analysis that is stuck in PENDING with no live job — e.g. the
     * process died mid-request. Uses the moment's age as a staleness proxy so a
     * legitimately in-flight analysis is left alone.
     */
    private fun reconcileStalePending() {
        if (reconciledPending) return
        if (_state.value.analysis != MomentAnalysisState.Pending) return
        val moment = _state.value.moment ?: return
        val ageMs = Clock.System.now().toEpochMilliseconds() -
            moment.createdAt.toEpochMilliseconds()
        if (ageMs > STALE_PENDING_MS) {
            reconciledPending = true
            runAnalysis()
        }
    }
}
