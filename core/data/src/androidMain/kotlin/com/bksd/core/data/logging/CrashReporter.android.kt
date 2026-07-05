package com.bksd.core.data.logging

import android.content.Context
import com.bksd.core.data.BuildKonfig
import io.sentry.Sentry
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid

actual object CrashReporter {
    actual fun capture(throwable: Throwable) {
        Sentry.captureException(throwable)
    }
}

fun installAndroidCrashReporter(context: Context, isDebug: Boolean) {
    val dsn = BuildKonfig.SENTRY_DSN_ANDROID
    if (isDebug || dsn.isBlank()) return
    SentryAndroid.init(context) { options ->
        options.dsn = dsn
        options.environment = "production"
        options.tracesSampleRate = 0.1
        options.isSendDefaultPii = false
        options.beforeSend = SentryOptions.BeforeSendCallback { event, _ ->
            event.request = null
            event.user = null
            event
        }
    }
}
