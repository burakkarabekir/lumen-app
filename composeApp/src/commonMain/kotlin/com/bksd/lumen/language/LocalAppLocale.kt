package com.bksd.lumen.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.key

expect object LocalAppLocale {
    val current: String
        @Composable get

    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}

@Composable
fun AppEnvironment(localeTag: String?, content: @Composable () -> Unit) {
    val tag = localeTag?.takeIf { it.isNotBlank() }
    CompositionLocalProvider(LocalAppLocale provides tag) {
        key(tag) {
            content()
        }
    }
}
