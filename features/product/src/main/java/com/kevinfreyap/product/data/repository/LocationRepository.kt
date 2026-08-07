package com.kevinfreyap.product.data.repository

import com.kevinfreyap.database.dao.LocationDao
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.repository.ILocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDao: LocationDao
): ILocationRepository {
    override fun getAllLocation(): Flow<List<Location>> {
        return locationDao.getAllLocation().map { locationEntities ->
            locationEntities.toDomain()
        }
    }

    override suspend fun getLocationByName(name: String): Location? {
        return locationDao.getLocationByName(name)?.toDomain()
    }

    override suspend fun insertLocation(location: Location) {
        locationDao.insertLocation(location.toEntity())
    }
}