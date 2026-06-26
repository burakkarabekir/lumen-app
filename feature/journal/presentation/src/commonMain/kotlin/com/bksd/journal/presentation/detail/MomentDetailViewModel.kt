package com.bksd.journal.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.core.presentation.util.toUiText
import com.bksd.journal.domain.usecase.DeleteMomentUseCase
import com.bksd.journal.domain.usecase.GetMomentUseCase
import com.bksd.journal.domain.usecase.UpdateMomentUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MomentDetailViewModel(
    private val getMomentUseCase: GetMomentUseCase,
    private val deleteMomentUseCase: DeleteMomentUseCase,
    private val updateMomentUseCase: UpdateMomentUseCase,
    private val momentId: String,
    private val initialIsEditing: Boolean = false
) : BaseViewModel<MomentDetailAction, MomentDetailEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MomentDetailState(isLoading = true))

    val state: StateFlow<MomentDetailState> = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMoment()
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
            MomentDetailAction.OnDeleteClick -> handleDelete()
            MomentDetailAction.OnShareClick -> Unit
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
        }
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
                editTags = moment.tags.toPersistentList()
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
            tags = current.editTags.toList()
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
                    sendEvent(MomentDetailEvent.ShowSuccess(UiText.Dynamic("Changes saved")))
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
                }
            }
        }
    }

    private fun handleDelete() {
        launch {
            when (val result = deleteMomentUseCase(momentId)) {
                is Result.Success -> {
                    sendEvent(MomentDetailEvent.ShowSuccess(UiText.Dynamic("Moment deleted")))
                    sendEvent(MomentDetailEvent.NavigateBack)
                }

                is Result.Error -> {
                    val errorText = result.error.toUiText()
                    sendEvent(MomentDetailEvent.ShowError(errorText))
                }
            }
        }
    }
}
