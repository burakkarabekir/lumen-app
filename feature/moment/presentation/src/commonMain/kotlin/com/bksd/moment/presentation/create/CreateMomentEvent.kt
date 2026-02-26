package com.bksd.moment.presentation.create

import com.bksd.core.presentation.util.UiText

sealed interface CreateMomentEvent {
    data object NavigateBack : CreateMomentEvent
    data class ShowSaveSuccess(val message: UiText) : CreateMomentEvent
    data class ShowError(val error: UiText) : CreateMomentEvent

    // Permission
    data object RequestAudioPermission : CreateMomentEvent

    // Launchers
    data object LaunchCamera : CreateMomentEvent
    data object LaunchPhotoPicker : CreateMomentEvent
    data object LaunchVideoPicker : CreateMomentEvent
    data object LaunchFilePicker : CreateMomentEvent
}
