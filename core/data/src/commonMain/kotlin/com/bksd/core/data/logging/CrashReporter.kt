package com.bksd.core.data.logging

expect object CrashReporter {
    fun capture(throwable: Throwable)
}
