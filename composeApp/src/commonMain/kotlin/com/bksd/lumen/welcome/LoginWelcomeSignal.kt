package com.bksd.lumen.welcome

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginWelcomeSignal {

    private val _pending = MutableStateFlow<WelcomeGreeting?>(null)
    val pending: StateFlow<WelcomeGreeting?> = _pending.asStateFlow()

    fun request(greeting: WelcomeGreeting) {
        _pending.value = greeting
    }

    fun clear() {
        _pending.value = null
    }
}
