package com.bksd.onboarding.presentation

import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for the Onboarding feature.
 * Manages pager state and emits navigation events.
 */
class OnboardingViewModel : BaseViewModel<OnboardingAction, OnboardingEvent>() {

    private var _state = OnboardingState()
        set(value) {
            field = value
            _stateFlow.value = value
        }

    private val _stateFlow = MutableStateFlow(_state)
    val state: StateFlow<OnboardingState> = _stateFlow

    override fun onAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.Next -> handleNext()
            OnboardingAction.Previous -> handlePrevious()
            OnboardingAction.Skip -> handleComplete()
            OnboardingAction.Complete -> handleComplete()
        }
    }

    private fun handleNext() {
        if (_state.isLastStep) {
            handleComplete()
        } else {
            _state = _state.copy(currentStep = _state.currentStep + 1)
        }
    }

    private fun handlePrevious() {
        if (!_state.isFirstStep) {
            _state = _state.copy(currentStep = _state.currentStep - 1)
        }
    }

    private fun handleComplete() {
        launch {
            sendEvent(OnboardingEvent.NavigateToAuth)
        }
    }
}
