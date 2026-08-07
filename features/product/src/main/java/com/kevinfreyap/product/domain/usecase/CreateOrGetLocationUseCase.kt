package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.model.LocationId
import com.kevinfreyap.product.domain.model.error.BatchLocationError
import com.kevinfreyap.product.domain.repository.ILocationRepository
import java.util.UUID
import javax.inject.Inject

class CreateOrGetLocationUseCase @Inject constructor(
    private val repository: ILocationRepository,
    private val validateProductLocation: ValidateBatchLocationUseCase
) {
    suspend operator fun invoke(rawLocation: String): Result<Location, BatchLocationError> {
        val sanitizedLocation = when (
            val validationResult = validateProductLocation(rawLocation)
        ) {
            is Result.Success -> validationResult.data
            is Result.Error -> return validationResult
        }

        val existingLocation = repository.getLocationByName(sanitizedLocation)

        return if (existingLocation != null) {
            Result.Success(existingLocation)
        } else {
            val newLocation = Location(
                locationId = LocationId("location-${UUID.randomUUID()}"),
                name = sanitizedLocation,
                description = null,
                locationBarcode = null,
                createdAt = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
            repository.insertLocation(newLocation)
            Result.Success(newLocation)
        }
    }
}