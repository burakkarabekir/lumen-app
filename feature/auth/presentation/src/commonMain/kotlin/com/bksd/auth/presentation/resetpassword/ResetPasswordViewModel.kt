package com.bksd.auth.presentation.resetpassword

import com.bksd.auth.domain.usecase.ResetPasswordUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel<ResetPasswordAction, ResetPasswordEvent>() {

    private val _state = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = _state.asStateFlow()

    override fun onAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.OnEmailChange ->
                _state.update { it.copy(email = action.email, error = null) }

            ResetPasswordAction.OnSubmitClick -> submit()
            ResetPasswordAction.OnBackToSignInClick -> sendEvent(ResetPasswordEvent.NavigateToSignIn)
        }
    }

    private fun submit() {
        if (!_state.value.isSubmitEnabled) return

        _state.update { it.copy(isLoading = true, error = null, isSuccess = false) }
        launch {
            when (val result = resetPasswordUseCase(_state.value.email)) {
                is Result.Error -> {
                    val errorText = result.error.toUiText()
                    _state.update { it.copy(isLoading = false, error = errorText) }
                    sendEvent(ResetPasswordEvent.ResetPasswordError(errorText))
                }

                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                    sendEvent(ResetPasswordEvent.ResetPasswordSuccess)
                }
            }
        }
    }
}
