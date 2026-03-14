package com.bksd.lumen.main

data class MainState(
    val isCheckingAuth: Boolean = true,
    val isLoggedIn: Boolean = false
)