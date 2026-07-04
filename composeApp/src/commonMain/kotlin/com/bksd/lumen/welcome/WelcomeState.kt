package com.bksd.lumen.welcome

data class WelcomeState(
    val greeting: WelcomeGreeting? = null,
    val firstName: String = "",
)
