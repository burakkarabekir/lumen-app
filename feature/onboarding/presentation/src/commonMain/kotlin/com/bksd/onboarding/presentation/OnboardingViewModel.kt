package com.bksd.onboarding.presentation

import com.bksd.onboarding.domain.repository.OnboardingRepository
import com.bksd.core.presentation.util.BaseViewModel

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
) : BaseViewModel<OnboardingAction, OnboardingEvent>() {

    override fun onAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.Complete -> complete()
        }
    }

    private fun complete() {
        launch {
            onboardingRepository.setCompleted()
            sendEvent(OnboardingEvent.Finished)
        }
    }
}
