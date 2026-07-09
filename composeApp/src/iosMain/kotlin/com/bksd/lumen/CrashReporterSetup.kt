package com.bksd.lumen

import com.bksd.core.data.logging.CrashReporterBridge

fun setCrashReporter(handler: (message: String) -> Unit) {
    CrashReporterBridge.handler = handler
}
