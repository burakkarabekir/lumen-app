package com.bksd.lumen.welcome

import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WelcomeViewModel(
    private val signal: LoginWelcomeSignal,
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage,
) : BaseViewModel<Nothing, Nothing>() {

    val state: StateFlow<WelcomeState> = signal.pending
        .map { greeting ->
            val resolved = when (greeting) {
                WelcomeGreeting.RETURNING ->
                    if (sessionStorage.consumeFirstLoginPending()) WelcomeGreeting.FIRST_LOGIN
                    else WelcomeGreeting.RETURNING

                WelcomeGreeting.NEW -> {
                    sessionStorage.consumeFirstLoginPending()
                    WelcomeGreeting.NEW
                }

                else -> greeting
            }
            WelcomeState(
                greeting = resolved,
                firstName = resolved?.let { firstNameOf(authRepository.getDisplayName()) }
                    .orEmpty(),
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, WelcomeState())

    fun consume() {
        signal.clear()
    }
}

private fun firstNameOf(displayName: String?): String =
    displayName?.trim()?.substringBefore(' ')?.takeIf { it.isNotBlank() }.orEmpty()
