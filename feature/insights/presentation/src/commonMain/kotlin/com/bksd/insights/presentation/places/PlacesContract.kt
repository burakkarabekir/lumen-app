package com.bksd.insights.presentation.places

import androidx.compose.runtime.Immutable
import com.bksd.insights.presentation.PlaceKind
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class PlacesState(
    val isLoading: Boolean = true,
    val query: String = "",
    val places: ImmutableList<PlaceUi> = persistentListOf(),
)

@Immutable
data class PlaceUi(
    val name: String,
    val count: Int,
    val lastEntryLabel: String,
    val kind: PlaceKind,
    val accentIndex: Int,
)

sealed interface PlacesAction {
    data class OnSearchChange(val query: String) : PlacesAction
    data object OnBack : PlacesAction
}

sealed interface PlacesEvent {
    data object NavigateBack : PlacesEvent
}
