package com.bksd.core.presentation.util

inline fun <T, R> List<T>?.onSafe(block: (List<T>) -> R): R? {
    return if (!this.isNullOrEmpty()) block(this) else null
}