package com.bksd.insights.presentation.places

import androidx.lifecycle.viewModelScope
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.insights.domain.model.Place
import com.bksd.insights.domain.usecase.GetPlacesUseCase
import com.bksd.insights.presentation.PlaceKind
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.monthAbbrRes
import com.bksd.insights.presentation.placeKindClassifier
import com.bksd.insights.presentation.place_date
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import org.jetbrains.compose.resources.getString

class PlacesViewModel(
    getPlaces: GetPlacesUseCase,
    private val timeZone: TimeZone,
) : BaseViewModel<PlacesAction, PlacesEvent>() {

    private val query = MutableStateFlow("")

    val state: StateFlow<PlacesState> = combine(getPlaces(), query) { places, q ->
        val classify = placeKindClassifier()
        val trimmed = q.trim()
        val filtered = if (trimmed.isBlank()) {
            places
        } else {
            places.filter { it.name.contains(trimmed, ignoreCase = true) }
        }
        PlacesState(
            isLoading = false,
            query = q,
            places = filtered.map { it.toUi(classify) }.toPersistentList(),
        )
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PlacesState(),
        )

    override fun onAction(action: PlacesAction) {
        when (action) {
            is PlacesAction.OnSearchChange -> query.value = action.query
            PlacesAction.OnBack -> sendEvent(PlacesEvent.NavigateBack)
        }
    }

    private suspend fun Place.toUi(classify: (String) -> PlaceKind): PlaceUi = PlaceUi(
        name = name,
        count = count,
        lastEntryLabel = formatLast(lastEntry),
        kind = classify(name),
        accentIndex = (name.hashCode() % ACCENT_COUNT + ACCENT_COUNT) % ACCENT_COUNT,
    )

    private suspend fun formatLast(instant: Instant): String {
        val date = instant.toLocalDateTime(timeZone).date
        return getString(Res.string.place_date, getString(monthAbbrRes(date.month)), date.day)
    }

    private companion object {
        const val ACCENT_COUNT = 5
    }
}
