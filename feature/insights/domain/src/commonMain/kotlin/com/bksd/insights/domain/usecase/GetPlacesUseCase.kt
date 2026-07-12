package com.bksd.insights.domain.usecase

import com.bksd.core.domain.model.Moment
import com.bksd.insights.domain.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPlacesUseCase(
    private val observeAllMoments: ObserveAllMomentsUseCase,
) {
    operator fun invoke(): Flow<List<Place>> =
        observeAllMoments().map { moments -> moments.toPlaces() }

    private fun List<Moment>.toPlaces(): List<Place> =
        asSequence()
            .mapNotNull { moment ->
                moment.location?.displayName
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                    ?.let { name -> name to moment.createdAt }
            }
            .groupBy { (name, _) -> name.lowercase() }
            .map { (_, entries) ->
                Place(
                    name = entries.first().first,
                    count = entries.size,
                    lastEntry = entries.maxOf { it.second },
                )
            }
            .sortedWith(
                compareByDescending<Place> { it.count }.thenByDescending { it.lastEntry },
            )
}
