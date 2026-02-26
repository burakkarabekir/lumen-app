package com.bksd.auth.presentation.signup

import com.bksd.auth.domain.usecase.SignUpUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpAction, SignUpEvent>() {

    private var _state = SignUpState()
        set(value) {
            field = value
            _stateFlow.value = value
        }

    private val _stateFlow = MutableStateFlow(_state)
    val state: StateFlow<SignUpState> = _stateFlow

    override fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnFullNameChange -> _state =
                _state.copy(fullName = action.name, error = null)

            is SignUpAction.OnEmailChange -> _state =
                _state.copy(email = action.email, error = null)

            is SignUpAction.OnPasswordChange -> _state =
                _state.copy(password = action.password, error = null)

            SignUpAction.OnSignUpClick -> signUp()
            SignUpAction.OnSignInClick -> sendEvent(SignUpEvent.NavigateToSignIn)
            SignUpAction.OnPrivacyClick -> sendEvent(SignUpEvent.OpenPrivacyPolicy)
            SignUpAction.OnTermsClick -> sendEvent(SignUpEvent.OpenTermsOfService)
        }
    }

    private fun signUp() {
        if (!_state.isSubmitEnabled) return

        _state = _state.copy(isLoading = true, error = null)
        launch {
            when (val result = signUpUseCase(_state.email, _state.password, _state.fullName)) {
                is Result.Error -> {
                    val errorText =
                        UiText.Dynamic(result.error.toString()) // TODO: Map AppError to proper UiText
                    _state = _state.copy(isLoading = false, error = errorText)
                    sendEvent(SignUpEvent.SignUpError(errorText))
                }

                is Result.Success -> {
                    _state = _state.copy(isLoading = false)
                    sendEvent(SignUpEvent.SignUpSuccess)
                }
            }
        }
    }
}
