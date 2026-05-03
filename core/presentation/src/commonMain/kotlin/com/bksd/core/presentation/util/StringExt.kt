package com.bksd.core.presentation.util

inline fun String?.onSafe(block: (String) -> Unit) {
    val trimmed = this?.trim()
    if (!trimmed.isNullOrEmpty()) {
        block(trimmed)
    }
}