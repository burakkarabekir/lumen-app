package com.bksd.lumen.main

sealed interface MainEvent {
    data object OnSessionExpired : MainEvent
}
