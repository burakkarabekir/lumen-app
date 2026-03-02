package com.bksd.moment.presentation.create.mappers

import com.bksd.core.domain.location.LocationData
import com.bksd.moment.presentation.create.LocationInfoUiModel

fun LocationData.toLocationInfoUiModel() = LocationInfoUiModel(
    coordinates = Pair(latitude, longitude),
    displayName = displayName
)
fun LocationInfoUiModel.toLocationData() = LocationData(
    latitude = coordinates.first,
    longitude = coordinates.second,
    displayName = displayName
)