package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.product.data.network.dto.request.LocationRequest
import com.kevinfreyap.product.data.network.dto.response.LocationResponse
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

fun Location.toEntity(syncState: SyncState): LocationEntity {
    return LocationEntity(
        locationId = this.locationId.value,
        name = this.name,
        description = this.description,
        locationBarcode = this.locationBarcode,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated,
        syncState = syncState
    )
}

// Sync
fun LocationResponse.toEntity(): LocationEntity {
    return LocationEntity(
        locationId = this.locationId,
        name = this.name,
        description = this.description,
        locationBarcode = this.locationBarcode,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated,
        syncState = SyncState.SYNCED
    )
}

fun LocationEntity.toRequest(): LocationRequest {
    return LocationRequest(
        locationId = this.locationId,
        name = this.name,
        description = this.description,
        locationBarcode = this.locationBarcode,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated
    )
}