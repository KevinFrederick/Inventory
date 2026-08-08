package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.model.LocationId
import com.kevinfreyap.product.domain.repository.ILocationRepository
import java.util.UUID
import javax.inject.Inject

class CreateOrGetLocationUseCase @Inject constructor(
    private val repository: ILocationRepository,
) {
    suspend operator fun invoke(validLocationName: String): Location {
        val existingLocation = repository.getLocationByName(validLocationName)

        return if (existingLocation != null) {
            existingLocation
        } else {
            val newLocation = Location(
                locationId = LocationId("location-${UUID.randomUUID()}"),
                name = validLocationName,
                description = null,
                locationBarcode = null,
                createdAt = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
            repository.insertLocation(newLocation)
            newLocation
        }
    }
}