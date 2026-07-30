package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.model.LocationId

fun LocationEntity.toDomain(): Location {
    return Location(
        locationId = LocationId(this.locationId),
        name = this.name,
        description = this.description,
        locationBarcode = this.locationBarcode,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated
    )
}

fun List<LocationEntity>.toDomain(): List<Location> {
    return this.map { entity ->
        entity.toDomain()
    }
}