package com.bksd.auth.presentation.signin

import com.bksd.auth.domain.usecase.SignInUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignInViewModel(
    private val signInUseCase: SignInUseCase
) : BaseViewModel<SignInAction, SignInEvent>() {

    private var _state = SignInState()
        set(value) {
            field = value
            _stateFlow.value = value
        }

    private val _stateFlow = MutableStateFlow(_state)
    val state: StateFlow<SignInState> = _stateFlow

    override fun onAction(action: SignInAction) {
        when (action) {
            is SignInAction.OnEmailChange -> _state =
                _state.copy(email = action.email, error = null)

            is SignInAction.OnPasswordChange -> _state =
                _state.copy(password = action.password, error = null)

            SignInAction.OnSignInClick -> signIn()
            SignInAction.OnSignUpClick -> sendEvent(SignInEvent.NavigateToSignUp)
            SignInAction.OnForgotPasswordClick -> sendEvent(SignInEvent.NavigateToForgotPassword)
        }
    }

    private fun signIn() {
        if (!_state.isSubmitEnabled) return

        _state = _state.copy(isLoading = true, error = null)
        launch {
            when (val result = signInUseCase(_state.email, _state.password)) {
                is Result.Error -> {
                    val errorText =
                        UiText.Dynamic(result.error.toString()) // TODO: Map AppError to proper UiText
                    _state = _state.copy(isLoading = false, error = errorText)
                    sendEvent(SignInEvent.SignInError(errorText))
                }

                is Result.Success -> {
                    _state = _state.copy(isLoading = false)
                    sendEvent(SignInEvent.SignInSuccess)
                }
            }
        }
    }
}
