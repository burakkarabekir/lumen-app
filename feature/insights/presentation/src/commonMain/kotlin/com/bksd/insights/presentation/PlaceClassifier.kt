package com.bksd.insights.presentation

import org.jetbrains.compose.resources.getStringArray

private val PLACE_SEPARATORS = Regex("[\\s,./()\\-]+")

internal suspend fun placeKindClassifier(): (String) -> PlaceKind {
    val beach = getStringArray(Res.array.place_beach_words)
    val park = getStringArray(Res.array.place_park_words)
    val restaurant = getStringArray(Res.array.place_restaurant_words)
    val landmark = getStringArray(Res.array.place_landmark_words)
    return { name -> classifyPlace(name, beach, park, restaurant, landmark) }
}

private fun classifyPlace(
    name: String,
    beach: List<String>,
    park: List<String>,
    restaurant: List<String>,
    landmark: List<String>,
): PlaceKind {
    val tokens = name.lowercase().split(PLACE_SEPARATORS)
    fun matches(words: List<String>) = tokens.any { token -> words.any { token.startsWith(it) } }
    return when {
        matches(beach) -> PlaceKind.BEACH
        matches(park) -> PlaceKind.PARK
        matches(restaurant) -> PlaceKind.RESTAURANT
        matches(landmark) -> PlaceKind.LANDMARK
        else -> PlaceKind.GENERIC
    }
}
