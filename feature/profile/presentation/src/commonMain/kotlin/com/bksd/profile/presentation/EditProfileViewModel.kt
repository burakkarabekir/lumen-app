package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.core.presentation.util.toUiText
import com.bksd.profile.domain.usecase.GetUserProfileUseCase
import com.bksd.profile.domain.usecase.SetProfileAvatarUseCase
import com.bksd.profile.domain.usecase.UpdateDisplayNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val setProfileAvatarUseCase: SetProfileAvatarUseCase,
    private val updateDisplayNameUseCase: UpdateDisplayNameUseCase,
) : BaseViewModel<EditProfileAction, EditProfileEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadProfile()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EditProfileState()
        )

    private fun loadProfile() {
        launch {
            when (val result = getUserProfileUseCase()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            name = result.data.displayName,
                            photoUrl = result.data.photoUrl,
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    override fun onAction(action: EditProfileAction) {
        when (action) {
            is EditProfileAction.OnNameChange -> {
                _state.update { it.copy(name = action.name) }
            }

            EditProfileAction.OnChangePhotoClick -> sendEvent(EditProfileEvent.OpenPhotoPicker)

            is EditProfileAction.OnPictureSelected -> uploadAvatar(action.bytes, action.mimeType)

            EditProfileAction.OnSaveClick -> handleSave()
        }
    }

    private fun uploadAvatar(bytes: ByteArray, mimeType: String?) {
        _state.update { it.copy(isAvatarLoading = true) }
        launch {
            when (val result = setProfileAvatarUseCase(bytes, mimeType)) {
                is Result.Success -> {
                    _state.update { it.copy(photoUrl = result.data, isAvatarLoading = false) }
                }

                is Result.Error -> {
                    _state.update { it.copy(isAvatarLoading = false) }
                    sendEvent(EditProfileEvent.ShowError(result.error.toUiText()))
                }
            }
        }
    }

    private fun handleSave() {
        val name = _state.value.name.trim()
        if (name.isEmpty()) {
            sendEvent(EditProfileEvent.ShowError(UiText.Dynamic("Name can't be empty")))
            return
        }
        _state.update { it.copy(isSaving = true) }
        launch {
            when (val result = updateDisplayNameUseCase(name)) {
                is Result.Success -> {
                    _state.update { it.copy(isSaving = false) }
                    sendEvent(EditProfileEvent.NavigateBack)
                }

                is Result.Error -> {
                    _state.update { it.copy(isSaving = false) }
                    sendEvent(EditProfileEvent.ShowError(result.error.toUiText()))
                }
            }
        }
    }
}
