package com.bksd.core.data.logging

object CrashReporterBridge {
    var handler: ((String) -> Unit)? = null
}

actual object CrashReporter {
    actual fun capture(throwable: Throwable) {
        CrashReporterBridge.handler?.invoke(throwable.stackTraceToString())
    }
}
