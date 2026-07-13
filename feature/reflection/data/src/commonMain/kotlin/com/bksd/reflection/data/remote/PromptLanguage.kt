package com.bksd.reflection.data.remote

import com.bksd.core.data.language.deviceLanguageTag
import com.bksd.core.domain.language.AppLanguage

internal fun AppLanguage.promptLanguageName(): String =
    when (tag ?: deviceLanguageTag()) {
        "tr" -> "Turkish"
        "de" -> "German"
        "es" -> "Spanish"
        "fr" -> "French"
        else -> "English"
    }
