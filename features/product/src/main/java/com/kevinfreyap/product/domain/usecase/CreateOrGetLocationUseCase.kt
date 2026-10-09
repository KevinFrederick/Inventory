package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.model.LocationId
import com.kevinfreyap.product.domain.repository.ILocationRepository
import java.util.UUID
import javax.inject.Inject

class CreateOrGetLocationUseCase @Inject constructor(
    private val repository: ILocationRepository,
    private val syncManager: ISyncManager
) {
    suspend operator fun invoke(validLocationName: String, groupId: GroupId): Location {
        val existingLocation = repository.getLocationByName(validLocationName, groupId)

        return if (existingLocation != null) {
            existingLocation
        } else {
            val newLocation = Location(
                locationId = LocationId("location-${UUID.randomUUID()}"),
                groupId = groupId,
                name = validLocationName,
                description = null,
                locationBarcode = null,
                createdAt = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
            repository.insertLocation(newLocation)
            syncManager.triggerSync()
            newLocation
        }
    }
}