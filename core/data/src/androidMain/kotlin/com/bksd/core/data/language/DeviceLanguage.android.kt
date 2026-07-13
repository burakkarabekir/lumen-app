package com.bksd.core.data.language

import java.util.Locale

actual fun deviceLanguageTag(): String = Locale.getDefault().language.lowercase()
