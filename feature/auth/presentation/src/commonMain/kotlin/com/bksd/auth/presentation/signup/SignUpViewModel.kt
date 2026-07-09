package com.bksd.auth.presentation.signup

import com.bksd.auth.domain.usecase.SignUpUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpAction, SignUpEvent>() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    override fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnFullNameChange ->
                _state.update { it.copy(fullName = action.name, error = null) }

            is SignUpAction.OnEmailChange ->
                _state.update { it.copy(email = action.email, error = null) }

            is SignUpAction.OnPasswordChange ->
                _state.update { it.copy(password = action.password, error = null) }

            is SignUpAction.OnTermsToggle ->
                _state.update { it.copy(agreedToTerms = action.agreed) }

            SignUpAction.OnSignUpClick -> signUp()
            SignUpAction.OnSignInClick -> sendEvent(SignUpEvent.NavigateToSignIn)
            SignUpAction.OnPrivacyClick -> sendEvent(SignUpEvent.OpenPrivacyPolicy)
            SignUpAction.OnTermsClick -> sendEvent(SignUpEvent.OpenTermsOfService)
        }
    }

    private fun signUp() {
        if (!_state.value.isSubmitEnabled) return

        _state.update { it.copy(isLoading = true, error = null) }
        launch {
            when (val result =
                signUpUseCase(_state.value.email, _state.value.password, _state.value.fullName)) {
                is Result.Error -> {
                    val errorText = result.error.toUiText()
                    _state.update { it.copy(isLoading = false, error = errorText) }
                    sendEvent(SignUpEvent.SignUpError(errorText))
                }

                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, awaitingConfirmation = true) }
                }
            }
        }
    }
}
