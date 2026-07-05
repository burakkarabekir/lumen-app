package com.bksd.lumen.lock

data class LockGateState(
    val enabled: Boolean = false,
    val loaded: Boolean = false,
)
