package com.bksd.auth.presentation.resetpassword

import com.bksd.auth.domain.usecase.ResetPasswordUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel<ResetPasswordAction, ResetPasswordEvent>() {

    private var _state = ResetPasswordState()
        set(value) {
            field = value
            _stateFlow.value = value
        }

    private val _stateFlow = MutableStateFlow(_state)
    val state: StateFlow<ResetPasswordState> = _stateFlow

    override fun onAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.OnEmailChange -> _state =
                _state.copy(email = action.email, error = null)

            ResetPasswordAction.OnSubmitClick -> submit()
            ResetPasswordAction.OnBackToSignInClick -> sendEvent(ResetPasswordEvent.NavigateToSignIn)
        }
    }

    private fun submit() {
        if (!_state.isSubmitEnabled) return

        _state = _state.copy(isLoading = true, error = null, isSuccess = false)
        launch {
            when (val result = resetPasswordUseCase(_state.email)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString()) // TODO: Map AppError
                    _state = _state.copy(isLoading = false, error = errorText)
                    sendEvent(ResetPasswordEvent.ResetPasswordError(errorText))
                }

                is Result.Success -> {
                    _state = _state.copy(isLoading = false, isSuccess = true)
                    sendEvent(ResetPasswordEvent.ResetPasswordSuccess)
                }
            }
        }
    }
}
