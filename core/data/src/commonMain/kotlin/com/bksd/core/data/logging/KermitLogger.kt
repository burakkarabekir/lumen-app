package com.bksd.core.data.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.bksd.core.domain.logging.AppLogger

fun configureAppLogging(isDebug: Boolean) {
    Logger.setMinSeverity(if (isDebug) Severity.Verbose else Severity.Warn)
}

object KermitLogger: AppLogger {

    override fun debug(message: String) {
        Logger.d(message)
    }

    override fun info(message: String) {
        Logger.i(message)
    }

    override fun warn(message: String) {
        Logger.w(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        Logger.e(message, throwable)
    }
}