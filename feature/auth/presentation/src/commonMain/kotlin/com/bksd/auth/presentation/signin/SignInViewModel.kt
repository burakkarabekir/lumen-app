package com.bksd.auth.presentation.signin

import com.bksd.auth.domain.usecase.GoogleSignInUseCase
import com.bksd.auth.domain.usecase.SignInUseCase
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.AuthErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val sessionStorage: SessionStorage,
) : BaseViewModel<SignInAction, SignInEvent>() {

    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()

    override fun onAction(action: SignInAction) {
        when (action) {
            is SignInAction.OnEmailChange ->
                _state.update { it.copy(email = action.email, error = null) }

            is SignInAction.OnPasswordChange ->
                _state.update { it.copy(password = action.password, error = null) }

            is SignInAction.OnRememberMeToggle ->
                _state.update { it.copy(rememberMe = action.enabled) }

            SignInAction.OnSignInClick -> signIn()
            SignInAction.OnSignUpClick -> sendEvent(SignInEvent.NavigateToSignUp)
            SignInAction.OnForgotPasswordClick -> sendEvent(SignInEvent.NavigateToForgotPassword)

            is SignInAction.OnGoogleIdTokenReceived -> signInWithGoogle(action.idToken)
            is SignInAction.OnGoogleSignInFailed -> onGoogleSignInFailed(action.cancelled)
        }
    }

    private fun signInWithGoogle(idToken: String) {
        _state.update { it.copy(isSocialLoading = true, error = null) }
        launch { handleSocialSignInResult(googleSignInUseCase(idToken)) }
    }

    private fun onGoogleSignInFailed(cancelled: Boolean) {
        if (cancelled) {
            _state.update { it.copy(isSocialLoading = false) }
        } else {
            val errorText = AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED).toUiText()
            _state.update { it.copy(isSocialLoading = false, error = errorText) }
            sendEvent(SignInEvent.SignInError(errorText))
        }
    }

    private fun signIn() {
        if (!_state.value.isSubmitEnabled) return

        _state.update { it.copy(isLoading = true, error = null) }
        launch {
            when (val result = signInUseCase(_state.value.email, _state.value.password)) {
                is Result.Error -> {
                    val errorText = result.error.toUiText()
                    _state.update { it.copy(isLoading = false, error = errorText) }
                    sendEvent(SignInEvent.SignInError(errorText))
                }

                is Result.Success -> {
                    sessionStorage.setRememberMe(_state.value.rememberMe)
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(SignInEvent.SignInSuccess)
                }
            }
        }
    }

    private suspend fun handleSocialSignInResult(result: Result<Unit, AppError>) {
        when (result) {
            is Result.Success -> {
                sessionStorage.setRememberMe(true)
                _state.update { it.copy(isSocialLoading = false) }
                sendEvent(SignInEvent.SignInSuccess)
            }

            is Result.Error -> {
                val isCancelled = result.error is AppError.Auth &&
                        (result.error as AppError.Auth).type == AuthErrorType.SOCIAL_LOGIN_CANCELLED
                if (isCancelled) {
                    _state.update { it.copy(isSocialLoading = false) }
                } else {
                    val errorText = result.error.toUiText()
                    _state.update { it.copy(isSocialLoading = false, error = errorText) }
                    sendEvent(SignInEvent.SignInError(errorText))
                }
            }
        }
    }
}
