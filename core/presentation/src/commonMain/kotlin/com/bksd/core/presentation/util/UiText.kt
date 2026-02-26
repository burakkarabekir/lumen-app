package com.bksd.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {
    data class Dynamic(val value: String): UiText
    class Resource(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is Dynamic -> value
            is Resource -> stringResource(
                resource = id,
                *args
            )
        }
    }

    suspend fun asStringAsync(): String {
        return when(this) {
            is Dynamic -> value
            is Resource -> getString(
                resource = id,
                *args
            )
        }
    }
}