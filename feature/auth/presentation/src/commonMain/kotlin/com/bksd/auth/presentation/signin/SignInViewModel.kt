package com.bksd.auth.presentation.signin

import com.bksd.auth.domain.usecase.SignInUseCase
import com.bksd.auth.domain.usecase.SignInWithAppleUseCase
import com.bksd.auth.domain.usecase.SignInWithGoogleUseCase
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.AuthErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithAppleUseCase: SignInWithAppleUseCase,
    private val sessionStorage: SessionStorage,
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

            is SignInAction.OnRememberMeToggle -> _state =
                _state.copy(rememberMe = action.enabled)

            SignInAction.OnSignInClick -> signIn()
            is SignInAction.OnGoogleSignInClick -> signInWithGoogle(action.platformContext)
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
                        UiText.Dynamic(result.error.toString())
                    _state = _state.copy(isLoading = false, error = errorText)
                    sendEvent(SignInEvent.SignInError(errorText))
                }

                is Result.Success -> {
                    sessionStorage.setRememberMe(_state.rememberMe)
                    _state = _state.copy(isLoading = false)
                    sendEvent(SignInEvent.SignInSuccess)
                }
            }
        }
    }

    private fun signInWithGoogle(platformContext: Any?) {
        if (_state.isLoading || _state.isSocialLoading) return
        _state = _state.copy(isSocialLoading = true, error = null)
        launch {
            handleSocialSignInResult(signInWithGoogleUseCase(platformContext))
        }
    }

    private fun signInWithApple(platformContext: Any?) {
        if (_state.isLoading || _state.isSocialLoading) return
        _state = _state.copy(isSocialLoading = true, error = null)
        launch {
            handleSocialSignInResult(signInWithAppleUseCase(platformContext))
        }
    }

    private suspend fun handleSocialSignInResult(result: Result<Unit, AppError>) {
        when (result) {
            is Result.Success -> {
                sessionStorage.setRememberMe(true)
                _state = _state.copy(isSocialLoading = false)
                sendEvent(SignInEvent.SignInSuccess)
            }
            is Result.Error -> {
                val isCancelled = result.error is AppError.Auth &&
                        (result.error as AppError.Auth).type == AuthErrorType.SOCIAL_LOGIN_CANCELLED
                if (isCancelled) {
                    _state = _state.copy(isSocialLoading = false)
                } else {
                    val errorText = UiText.Dynamic(result.error.toString())
                    _state = _state.copy(isSocialLoading = false, error = errorText)
                    sendEvent(SignInEvent.SignInError(errorText))
                }
            }
        }
    }
}
