package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.presentation.model.LocationUi

fun Location.toUi(): LocationUi {
    return LocationUi(
        id = this.locationId.value,
        name = this.name
    )
}

fun List<Location>.toUi(): List<LocationUi> {
    return this.map { location ->
        location.toUi()
    }
}