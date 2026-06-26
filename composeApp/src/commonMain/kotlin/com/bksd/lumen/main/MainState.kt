package com.bksd.lumen.main

import androidx.compose.runtime.Immutable

@Immutable
data class MainState(
    val isLoggedIn: Boolean = false,
    val isReady: Boolean = false
)