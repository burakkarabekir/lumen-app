package com.bksd.core.design_system.component.selection_list

import androidx.compose.runtime.Immutable

@Immutable
data class Selectable<T>(
    val item: T,
    val selected: Boolean,
) {
    companion object {
        fun <T> List<T>.asUnselectedItems(): List<Selectable<T>> {
            return map {
                Selectable(
                    item = it,
                    selected = false
                )
            }
        }
    }
}